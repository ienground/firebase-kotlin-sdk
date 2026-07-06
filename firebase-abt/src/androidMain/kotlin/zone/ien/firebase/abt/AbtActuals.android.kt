package zone.ien.firebase.abt

public actual typealias AbtException = com.google.firebase.abt.AbtException

public actual class AbtExperimentInfo internal actual constructor() {
    private var values: Map<String, String> = emptyMap()

    internal constructor(androidInfo: com.google.firebase.abt.AbtExperimentInfo) : this() {
        values = androidInfo.toExperimentValues()
    }

    public actual val experimentId: String
        get() = values[EXPERIMENT_ID_KEY].orEmpty()
    public actual val variantId: String
        get() = values[VARIANT_ID_KEY].orEmpty()
}

public actual class FirebaseABTesting internal actual constructor() {
    private var androidAbt: com.google.firebase.abt.FirebaseABTesting? = null

    internal constructor(androidAbt: com.google.firebase.abt.FirebaseABTesting) : this() {
        this.androidAbt = androidAbt
    }

    public actual fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>, originService: String) {
        androidAbt?.replaceAllExperiments(replacementExperiments)
    }

    public actual fun removeAllExperiments(originService: String) {
        androidAbt?.removeAllExperiments()
    }

    public actual fun getAllExperiments(originService: String): List<AbtExperimentInfo> {
        return androidAbt?.getAllExperiments()?.map { AbtExperimentInfo(it) } ?: emptyList()
    }
}

private const val EXPERIMENT_ID_KEY = "experimentId"
private const val VARIANT_ID_KEY = "variantId"

private fun com.google.firebase.abt.AbtExperimentInfo.toExperimentValues(): Map<String, String> {
    return runCatching {
        val method = javaClass.getDeclaredMethod("toStringMap")
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        method.invoke(this) as? Map<String, String> ?: emptyMap()
    }.getOrDefault(emptyMap())
}
