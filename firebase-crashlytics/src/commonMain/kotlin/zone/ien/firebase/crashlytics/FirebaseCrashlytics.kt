package zone.ien.firebase.crashlytics

public expect class FirebaseCrashlytics {
    public fun log(message: String)
    public fun recordException(throwable: Throwable)
    public fun setCustomKey(key: String, value: String)
    public fun setCustomKey(key: String, value: Boolean)
    public fun setCustomKey(key: String, value: Double)
    public fun setCustomKey(key: String, value: Float)
    public fun setCustomKey(key: String, value: Int)
    public fun setCustomKey(key: String, value: Long)
    public fun setUserId(identifier: String)
    public fun setCrashlyticsCollectionEnabled(enabled: Boolean)
    public fun deleteUnsentReports()
    public fun sendUnsentReports()
    public suspend fun checkForUnsentReports(): Boolean
    public fun didCrashOnPreviousExecution(): Boolean
 
    public companion object {
        public fun getInstance(): FirebaseCrashlytics
    }
}
