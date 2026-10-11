package zone.ien.firebase.database

public expect class DatabaseReference : Query {
    public val key: String?
    public fun child(pathString: String): DatabaseReference
    public fun push(): DatabaseReference

    public suspend fun setValue(value: Any?)
    public suspend fun removeValue()
    public suspend fun updateChildren(update: Map<String, Any?>)
    public suspend fun runTransaction(handler: (MutableData) -> TransactionResult): TransactionResult
    public fun keepSynced(keepSynced: Boolean)
}
