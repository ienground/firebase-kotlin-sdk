package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow

enum class QueryDirection {
    ASCENDING,
    DESCENDING
}

enum class WhereOperator {
    EQUAL,
    NOT_EQUAL,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    ARRAY_CONTAINS,
    ARRAY_CONTAINS_ANY,
    IN,
    NOT_IN
}

expect open class Query {
    suspend fun get(): QuerySnapshot
    suspend fun get(source: Source): QuerySnapshot
    fun snapshots(): Flow<QuerySnapshot>
    fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<QuerySnapshot>
    fun where(field: String, operator: WhereOperator, value: Any): Query
    fun orderBy(field: String, direction: QueryDirection = QueryDirection.ASCENDING): Query
    fun where(field: FieldPath, operator: WhereOperator, value: Any): Query
    fun orderBy(field: FieldPath, direction: QueryDirection = QueryDirection.ASCENDING): Query
    fun limit(limit: Long): Query
    fun limitToLast(limit: Long): Query
    fun startAt(document: DocumentSnapshot): Query
    fun startAfter(document: DocumentSnapshot): Query
    fun endAt(document: DocumentSnapshot): Query
    fun endBefore(document: DocumentSnapshot): Query

    fun count(): AggregateQuery
    fun sum(field: String): AggregateQuery
    fun sum(field: FieldPath): AggregateQuery
    fun average(field: String): AggregateQuery
    fun average(field: FieldPath): AggregateQuery
    fun aggregate(field: AggregateField, vararg fields: AggregateField): AggregateQuery
}
