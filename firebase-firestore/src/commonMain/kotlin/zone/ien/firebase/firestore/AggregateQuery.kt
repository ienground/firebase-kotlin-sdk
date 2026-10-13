package zone.ien.firebase.firestore

enum class AggregateSource {
    SERVER
}

expect class AggregateField {
    companion object {
        fun count(): AggregateField
        fun sum(field: String): AggregateField
        fun sum(field: FieldPath): AggregateField
        fun average(field: String): AggregateField
        fun average(field: FieldPath): AggregateField
    }
}

expect class AggregateQuery {
    suspend fun get(source: AggregateSource = AggregateSource.SERVER): AggregateQuerySnapshot
}

expect class AggregateQuerySnapshot {
    val count: Long
    fun get(field: AggregateField): Any?
    fun getLong(field: AggregateField): Long?
    fun getDouble(field: AggregateField): Double?
}
