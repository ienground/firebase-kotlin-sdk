package zone.ien.firebase.abt

import com.google.firebase.abt.component.AbtComponent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zone.ien.firebase.FirebaseApp

public actual class AbtException actual constructor(message: String?, cause: Throwable?) : Exception(message, cause) {
    public actual constructor(message: String?) : this(message, null)
}

public actual class AbtExperimentInfo internal actual constructor() {
    private var values: Map<String, String> = emptyMap()

    internal constructor(androidInfo: com.google.firebase.abt.AbtExperimentInfo) : this() {
        values = androidInfo.toAbtExperimentValues()
    }

    internal constructor(values: Map<String, String>) : this() {
        this.values = values.toMap()
    }

    public actual val experimentId: String
        get() = values[ABT_EXPERIMENT_ID_KEY].orEmpty()
    public actual val variantId: String
        get() = values[ABT_VARIANT_ID_KEY].orEmpty()
    public actual val triggerEvent: String
        get() = values[ABT_TRIGGER_EVENT_KEY].orEmpty()
    public actual val experimentStartTime: String
        get() = values[ABT_EXPERIMENT_START_TIME_KEY].orEmpty()
    public actual val triggerTimeoutMillis: Long
        get() = values[ABT_TRIGGER_TIMEOUT_KEY]?.toLongOrNull() ?: 0L
    public actual val timeToLiveMillis: Long
        get() = values[ABT_TIME_TO_LIVE_KEY]?.toLongOrNull() ?: 0L
}

public actual class FirebaseABTesting private constructor(
    public actual val originService: String,
    private val androidAbt: com.google.firebase.abt.FirebaseABTesting
) {
    private val operationLock = ReentrantLock()
    private val state = AtomicReference(AbtState())

    internal actual constructor(originService: String) : this(
        originService = normalizeAbtOriginService(originService),
        androidAbt = createAndroidDelegate(FirebaseApp.instance, normalizeAbtOriginService(originService))
    )

    public actual val isNativeExperimentApplicationSupported: Boolean
        get() = true

    public actual val requestedExperiments: List<AbtExperimentInfo>
        get() = state.get().requestedExperiments.map(::AbtExperimentInfo)

    public actual val lastRequest: AbtRequest?
        get() = state.get().lastRequest

    public actual val lastResult: AbtOperationResult?
        get() = state.get().lastResult

    public actual fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>) {
        replaceAllExperimentsBlocking(validateAbtExperiments(replacementExperiments))
    }

    public actual fun removeAllExperiments() {
        removeAllExperimentsBlocking()
    }

    public actual fun getAllExperiments(): List<AbtExperimentInfo> {
        return operationLock.withLock {
            wrapAndroidAbtException {
                androidAbt.getAllExperiments().map { AbtExperimentInfo(it) }
            }
        }
    }

    public actual suspend fun replaceAllExperimentsAsync(
        replacementExperiments: List<Map<String, String>>
    ): AbtOperationResult {
        val validatedExperiments = validateAbtExperiments(replacementExperiments)
        return withContext(Dispatchers.IO) {
            replaceAllExperimentsBlocking(validatedExperiments)
        }
    }

    public actual suspend fun removeAllExperimentsAsync(): AbtOperationResult =
        withContext(Dispatchers.IO) { removeAllExperimentsBlocking() }

    public actual suspend fun getAllExperimentsAsync(): List<AbtExperimentInfo> =
        withContext(Dispatchers.IO) { getAllExperimentsBlocking() }

    public actual fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>, originService: String) {
        requireMatchingOrigin(originService)
        replaceAllExperiments(replacementExperiments)
    }

    public actual fun removeAllExperiments(originService: String) {
        requireMatchingOrigin(originService)
        removeAllExperiments()
    }

    public actual fun getAllExperiments(originService: String): List<AbtExperimentInfo> {
        requireMatchingOrigin(originService)
        return getAllExperiments()
    }

    private fun requireMatchingOrigin(requestedOriginService: String) {
        require(normalizeAbtOriginService(requestedOriginService) == originService) {
            "FirebaseABTesting origin is fixed to '$originService'."
        }
    }

    private fun replaceAllExperimentsBlocking(
        validatedExperiments: List<Map<String, String>>
    ): AbtOperationResult = operationLock.withLock {
        wrapAndroidAbtException { androidAbt.replaceAllExperiments(validatedExperiments) }
        val result = AbtOperationResult(AbtApplicationStatus.APPLIED, true)
        state.set(
            AbtState(
                requestedExperiments = validatedExperiments,
                lastRequest = AbtRequest(AbtRequestType.REPLACE_ALL, validatedExperiments),
                lastResult = result
            )
        )
        result
    }

    private fun removeAllExperimentsBlocking(): AbtOperationResult = operationLock.withLock {
        wrapAndroidAbtException { androidAbt.removeAllExperiments() }
        val result = AbtOperationResult(AbtApplicationStatus.APPLIED, true)
        state.set(
            AbtState(
                requestedExperiments = emptyList(),
                lastRequest = AbtRequest(AbtRequestType.REMOVE_ALL, emptyList()),
                lastResult = result
            )
        )
        result
    }

    private fun getAllExperimentsBlocking(): List<AbtExperimentInfo> = operationLock.withLock {
        wrapAndroidAbtException {
            androidAbt.getAllExperiments().map(::AbtExperimentInfo)
        }
    }

    public actual companion object {
        private val instances = ConcurrentHashMap<String, FirebaseABTesting>()

        public actual fun getInstance(originService: String): FirebaseABTesting =
            getInstance(FirebaseApp.instance, originService)

        public actual fun getInstance(app: FirebaseApp, originService: String): FirebaseABTesting {
            val normalizedOriginService = normalizeAbtOriginService(originService)
            val key = "${app.getName()}:$normalizedOriginService"
            return instances.computeIfAbsent(key) {
                FirebaseABTesting(
                    originService = normalizedOriginService,
                    androidAbt = createAndroidDelegate(app, normalizedOriginService)
                )
            }
        }
    }
}

private data class AbtState(
    val requestedExperiments: List<Map<String, String>> = emptyList(),
    val lastRequest: AbtRequest? = null,
    val lastResult: AbtOperationResult? = null
)

private fun createAndroidDelegate(
    app: FirebaseApp,
    originService: String
): com.google.firebase.abt.FirebaseABTesting {
    return app.androidApp.get(AbtComponent::class.java).get(originService)
}

private inline fun <T> wrapAndroidAbtException(block: () -> T): T {
    try {
        return block()
    } catch (exception: com.google.firebase.abt.AbtException) {
        throw AbtException(exception.message, exception)
    }
}

internal fun com.google.firebase.abt.AbtExperimentInfo.toAbtExperimentValues(
    methodName: String = "toStringMap"
): Map<String, String> {
    try {
        val method = com.google.firebase.abt.AbtExperimentInfo::class.java.getDeclaredMethod(methodName)
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return method.invoke(this) as? Map<String, String>
            ?: throw AbtException("Firebase ABT experiment conversion returned an invalid value.")
    } catch (exception: AbtException) {
        throw exception
    } catch (exception: Throwable) {
        throw AbtException("Failed to convert Firebase ABT experiment information.", exception)
    }
}
