package zone.ien.firebase.abt

public actual class AbtException actual constructor(message: String?, cause: Throwable?) : Exception(message, cause) {
    public actual constructor(message: String?) : this(message, null)
}

public actual class AbtExperimentInfo internal actual constructor() {
    private var _experimentId: String = ""
    private var _variantId: String = ""

    internal constructor(experimentId: String, variantId: String) : this() {
        _experimentId = experimentId
        _variantId = variantId
    }

    public actual val experimentId: String get() = _experimentId
    public actual val variantId: String get() = _variantId
}

public actual class FirebaseABTesting internal actual constructor() {
    private val experiments = mutableListOf<AbtExperimentInfo>()

    public actual fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>, originService: String) {
        experiments.clear()
        replacementExperiments.forEach { map ->
            val info = AbtExperimentInfo(
                experimentId = map["experimentId"] ?: "",
                variantId = map["variantId"] ?: ""
            )
            experiments.add(info)
        }
    }

    public actual fun removeAllExperiments(originService: String) {
        experiments.clear()
    }

    public actual fun getAllExperiments(originService: String): List<AbtExperimentInfo> {
        return experiments.toList()
    }
}
