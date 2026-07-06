package zone.ien.firebase.abt

public expect class AbtException : Exception

public expect class AbtExperimentInfo internal constructor() {
    public val experimentId: String
    public val variantId: String
}

public expect class FirebaseABTesting {
    public fun replaceAllExperiments(replacementExperiments: List<Map<String, String>>, originService: String)
    public fun removeAllExperiments(originService: String)
    public fun getAllExperiments(originService: String): List<AbtExperimentInfo>
}
