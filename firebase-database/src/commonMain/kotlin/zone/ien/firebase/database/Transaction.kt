package zone.ien.firebase.database

public expect class MutableData {
    public val key: String?
    public val value: Any?
    public fun setValue(value: Any?)
}

public enum class TransactionResult {
    SUCCESS,
    ABORT
}
