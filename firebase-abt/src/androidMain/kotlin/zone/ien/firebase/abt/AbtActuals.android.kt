package zone.ien.firebase.abt

public actual typealias AbtException = com.google.firebase.abt.AbtException

public actual class AbtExperimentInfo(private val androidInfo: com.google.firebase.abt.AbtExperimentInfo) {
    public actual val experimentId: String
        get() = androidInfo.experimentId
    public actual val variantId: String
        get() = androidInfo.variantId
}

public actual class FirebaseABTesting internal actual constructor() {
    private var androidAbt: com.google.firebase.abt.FirebaseABTesting? = null

    internal constructor(androidAbt: com.google.firebase.abt.FirebaseABTesting) : this() {
        this.androidAbt = androidAbt
    }

    public actual fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>, originService: String) {
        androidAbt?.replaceAllExperiments(replacementExperiments, originService)
    }

    public actual fun removeAllExperiments(originService: String) {
        androidAbt?.removeAllExperiments(originService)
    }

    public actual fun getAllExperiments(originService: String): List<AbtExperimentInfo> {
        return androidAbt?.getAllExperiments(originService)?.map { AbtExperimentInfo(it) } ?: emptyList()
    }
}
