package zone.ien.firebase.database

public expect class DataSnapshot {
    public val key: String?
    public val value: Any?
    public fun child(path: String): DataSnapshot
    public fun exists(): Boolean
    public fun hasChild(path: String): Boolean
}
