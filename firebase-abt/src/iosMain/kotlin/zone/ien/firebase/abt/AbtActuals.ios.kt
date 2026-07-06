package zone.ien.firebase.abt

actual class AbtException(message: String?, cause: Throwable?) : Exception(message, cause) {
    constructor() : this(null, null)
    constructor(message: String?) : this(message, null)
}

public actual class AbtExperimentInfo internal actual constructor() {
    private var mockExperimentId: String = ""
    private var mockVariantId: String = ""

    public constructor(experimentId: String, variantId: String) : this() {
        this.mockExperimentId = experimentId
        this.mockVariantId = variantId
    }

    public actual val experimentId: String
        get() = mockExperimentId

    public actual val variantId: String
        get() = mockVariantId
}

public actual class FirebaseABTesting {
    private val memoryExperiments = mutableListOf<AbtExperimentInfo>()

    public actual fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>, originService: String) {
        memoryExperiments.clear()
        replacementExperiments.forEach { map ->
            val expId = map["experimentId"] ?: ""
            val varId = map["variantId"] ?: ""
            memoryExperiments.add(AbtExperimentInfo(expId, varId))
        }
    }

    public actual fun removeAllExperiments(originService: String) {
        memoryExperiments.clear()
    }

    public actual fun getAllExperiments(originService: String): List<AbtExperimentInfo> {
        return memoryExperiments.toList()
    }
}
