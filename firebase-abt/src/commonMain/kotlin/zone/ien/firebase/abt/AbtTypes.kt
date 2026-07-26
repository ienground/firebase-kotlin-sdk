package zone.ien.firebase.abt

import zone.ien.firebase.Firebase
import zone.ien.firebase.FirebaseApp

public expect class AbtException : Exception {
    public constructor(message: String?)
    public constructor(message: String?, cause: Throwable?)
}

public expect class AbtExperimentInfo internal constructor() {
    public val experimentId: String
    public val variantId: String
    public val triggerEvent: String
    public val experimentStartTime: String
    public val triggerTimeoutMillis: Long
    public val timeToLiveMillis: Long
}

public enum class AbtRequestType {
    REPLACE_ALL,
    REMOVE_ALL
}

public enum class AbtApplicationStatus {
    APPLIED,
    RECORDED_NOT_APPLIED
}

public data class AbtRequest(
    val type: AbtRequestType,
    val experiments: List<Map<String, String>>
)

public data class AbtOperationResult(
    val status: AbtApplicationStatus,
    val isNativeApplicationSupported: Boolean
)

public expect class FirebaseABTesting internal constructor(originService: String) {
    public val originService: String
    public val isNativeExperimentApplicationSupported: Boolean
    public val requestedExperiments: List<AbtExperimentInfo>
    public val lastRequest: AbtRequest?
    public val lastResult: AbtOperationResult?

    /** 이 호출은 현재 스레드를 차단할 수 있습니다. */
    @Deprecated("이 API는 현재 스레드를 차단할 수 있습니다. replaceAllExperimentsAsync를 사용하세요.")
    public fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>)
    /** 이 호출은 현재 스레드를 차단할 수 있습니다. */
    @Deprecated("이 API는 현재 스레드를 차단할 수 있습니다. removeAllExperimentsAsync를 사용하세요.")
    public fun removeAllExperiments()
    /** 이 호출은 현재 스레드를 차단할 수 있습니다. */
    @Deprecated("이 API는 현재 스레드를 차단할 수 있습니다. getAllExperimentsAsync를 사용하세요.")
    public fun getAllExperiments(): List<AbtExperimentInfo>

    public suspend fun replaceAllExperimentsAsync(
        replacementExperiments: List<Map<String, String>>
    ): AbtOperationResult
    public suspend fun removeAllExperimentsAsync(): AbtOperationResult
    public suspend fun getAllExperimentsAsync(): List<AbtExperimentInfo>

    /** 이 호출은 현재 스레드를 차단할 수 있습니다. */
    @Deprecated("origin은 인스턴스 생성 시 고정됩니다. replaceAllExperimentsAsync를 사용하세요.")
    public fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>, originService: String)
    /** 이 호출은 현재 스레드를 차단할 수 있습니다. */
    @Deprecated("origin은 인스턴스 생성 시 고정됩니다. removeAllExperimentsAsync를 사용하세요.")
    public fun removeAllExperiments(originService: String)
    /** 이 호출은 현재 스레드를 차단할 수 있습니다. */
    @Deprecated("origin은 인스턴스 생성 시 고정됩니다. getAllExperimentsAsync를 사용하세요.")
    public fun getAllExperiments(originService: String): List<AbtExperimentInfo>

    public companion object {
        public fun getInstance(originService: String): FirebaseABTesting
        public fun getInstance(app: FirebaseApp, originService: String): FirebaseABTesting
    }
}

public fun Firebase.abt(originService: String): FirebaseABTesting =
    FirebaseABTesting.getInstance(originService)

public fun Firebase.abt(app: FirebaseApp, originService: String): FirebaseABTesting =
    FirebaseABTesting.getInstance(app, originService)

internal const val ABT_EXPERIMENT_ID_KEY = "experimentId"
internal const val ABT_VARIANT_ID_KEY = "variantId"
internal const val ABT_TRIGGER_EVENT_KEY = "triggerEvent"
internal const val ABT_EXPERIMENT_START_TIME_KEY = "experimentStartTime"
internal const val ABT_TRIGGER_TIMEOUT_KEY = "triggerTimeoutMillis"
internal const val ABT_TIME_TO_LIVE_KEY = "timeToLiveMillis"

internal val REQUIRED_ABT_EXPERIMENT_KEYS: Set<String> = setOf(
    ABT_EXPERIMENT_ID_KEY,
    ABT_VARIANT_ID_KEY,
    ABT_EXPERIMENT_START_TIME_KEY,
    ABT_TRIGGER_TIMEOUT_KEY,
    ABT_TIME_TO_LIVE_KEY
)

internal fun normalizeAbtOriginService(originService: String): String = when (originService) {
    "frc", "fiam" -> originService
    "remote-config" -> "frc"
    "in-app-messaging" -> "fiam"
    else -> throw IllegalArgumentException(
        "Unsupported Firebase ABT origin '$originService'. Use 'frc' or 'fiam'."
    )
}

internal fun validateAbtExperiments(
    experiments: List<Map<String, String>>
): List<Map<String, String>> = experiments.mapIndexed { index, experiment ->
    REQUIRED_ABT_EXPERIMENT_KEYS.forEach { key ->
        if (experiment[key].isNullOrBlank()) {
            throw AbtException("Experiment at index $index is missing required field '$key'.")
        }
    }

    val startTime = experiment.getValue(ABT_EXPERIMENT_START_TIME_KEY)
    if (!isValidAbtStartTime(startTime)) {
        throw AbtException(
            "Experiment at index $index has invalid experimentStartTime '$startTime'. " +
                "Expected yyyy-MM-dd'T'HH:mm:ss."
        )
    }

    listOf(ABT_TRIGGER_TIMEOUT_KEY, ABT_TIME_TO_LIVE_KEY).forEach { key ->
        val value = experiment.getValue(key).toLongOrNull()
        if (value == null || value < 0L) {
            throw AbtException("Experiment at index $index has invalid '$key'.")
        }
    }

    experiment.toMap()
}

private val abtStartTimePattern =
    Regex("^(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})$")

private fun isValidAbtStartTime(value: String): Boolean {
    val match = abtStartTimePattern.matchEntire(value) ?: return false
    val (yearText, monthText, dayText, hourText, minuteText, secondText) = match.destructured
    val year = yearText.toInt()
    val month = monthText.toInt()
    val day = dayText.toInt()
    val maxDay = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 400 == 0 || year % 4 == 0 && year % 100 != 0) 29 else 28
        else -> return false
    }
    return day in 1..maxDay &&
        hourText.toInt() in 0..23 &&
        minuteText.toInt() in 0..59 &&
        secondText.toInt() in 0..59
}
