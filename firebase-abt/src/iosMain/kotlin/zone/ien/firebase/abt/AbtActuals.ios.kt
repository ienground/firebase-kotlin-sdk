package zone.ien.firebase.abt

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import zone.ien.firebase.FirebaseApp

public actual class AbtException actual constructor(message: String?, cause: Throwable?) : Exception(message, cause) {
    public actual constructor(message: String?) : this(message, null)
}

public actual class AbtExperimentInfo internal actual constructor() {
    private var values: Map<String, String> = emptyMap()

    internal constructor(values: Map<String, String>) : this() {
        this.values = values.toMap()
    }

    public actual val experimentId: String get() = values[ABT_EXPERIMENT_ID_KEY].orEmpty()
    public actual val variantId: String get() = values[ABT_VARIANT_ID_KEY].orEmpty()
    public actual val triggerEvent: String get() = values[ABT_TRIGGER_EVENT_KEY].orEmpty()
    public actual val experimentStartTime: String get() = values[ABT_EXPERIMENT_START_TIME_KEY].orEmpty()
    public actual val triggerTimeoutMillis: Long
        get() = values[ABT_TRIGGER_TIMEOUT_KEY]?.toLongOrNull() ?: 0L
    public actual val timeToLiveMillis: Long
        get() = values[ABT_TIME_TO_LIVE_KEY]?.toLongOrNull() ?: 0L
}

@OptIn(ExperimentalAtomicApi::class)
public actual class FirebaseABTesting internal actual constructor(
    originService: String
) {
    public actual val originService: String = normalizeAbtOriginService(originService)
    private val state: AtomicReference<AbtState> = AtomicReference(AbtState())
    private val operationMutex = Mutex()

    public actual val isNativeExperimentApplicationSupported: Boolean
        get() = false

    public actual val requestedExperiments: List<AbtExperimentInfo>
        get() = state.load().requestedExperiments.map(::AbtExperimentInfo)

    public actual val lastRequest: AbtRequest?
        get() = state.load().lastRequest

    public actual val lastResult: AbtOperationResult?
        get() = state.load().lastResult

    public actual fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>) {
        recordReplacement(validateAbtExperiments(replacementExperiments))
    }

    public actual fun removeAllExperiments() {
        recordRemoval()
    }

    public actual fun getAllExperiments(): List<AbtExperimentInfo> {
        return emptyList()
    }

    public actual suspend fun replaceAllExperimentsAsync(
        replacementExperiments: List<Map<String, String>>
    ): AbtOperationResult {
        val validatedExperiments = validateAbtExperiments(replacementExperiments)
        return operationMutex.withLock { recordReplacement(validatedExperiments) }
    }

    public actual suspend fun removeAllExperimentsAsync(): AbtOperationResult =
        operationMutex.withLock { recordRemoval() }

    public actual suspend fun getAllExperimentsAsync(): List<AbtExperimentInfo> = emptyList()

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

    private fun recordReplacement(
        validatedExperiments: List<Map<String, String>>
    ): AbtOperationResult {
        val result = AbtOperationResult(AbtApplicationStatus.RECORDED_NOT_APPLIED, false)
        state.store(
            AbtState(
                requestedExperiments = validatedExperiments,
                lastRequest = AbtRequest(AbtRequestType.REPLACE_ALL, validatedExperiments),
                lastResult = result
            )
        )
        return result
    }

    private fun recordRemoval(): AbtOperationResult {
        val result = AbtOperationResult(AbtApplicationStatus.RECORDED_NOT_APPLIED, false)
        state.store(
            AbtState(
                requestedExperiments = emptyList(),
                lastRequest = AbtRequest(AbtRequestType.REMOVE_ALL, emptyList()),
                lastResult = result
            )
        )
        return result
    }

    public actual companion object {
        private val instances: AtomicReference<Map<String, FirebaseABTesting>> =
            AtomicReference(emptyMap())

        public actual fun getInstance(originService: String): FirebaseABTesting =
            getInstance(FirebaseApp.instance, originService)

        public actual fun getInstance(app: FirebaseApp, originService: String): FirebaseABTesting {
            val normalizedOriginService = normalizeAbtOriginService(originService)
            val key = "${app.getName()}:$normalizedOriginService"
            while (true) {
                val current = instances.load()
                current[key]?.let { return it }

                val created = FirebaseABTesting(normalizedOriginService)
                if (instances.compareAndSet(current, current + (key to created))) {
                    return created
                }
            }
        }
    }
}

private data class AbtState(
    val requestedExperiments: List<Map<String, String>> = emptyList(),
    val lastRequest: AbtRequest? = null,
    val lastResult: AbtOperationResult? = null
)
