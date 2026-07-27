package zone.ien.firebase.firestore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

enum class QueryDirection {
    ASCENDING,
    DESCENDING
}

typealias Direction = QueryDirection

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
    val firestore: FirebaseFirestore
    val snapshots: Flow<QuerySnapshot>

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

    suspend fun get(): QuerySnapshot
    suspend fun get(source: Source): QuerySnapshot
    fun snapshots(): Flow<QuerySnapshot>
    fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<QuerySnapshot>
    fun where(field: String, operator: WhereOperator, value: Any?): Query
    fun orderBy(field: String, direction: QueryDirection = QueryDirection.ASCENDING): Query
    fun where(field: FieldPath, operator: WhereOperator, value: Any?): Query
    fun orderBy(field: FieldPath, direction: QueryDirection = QueryDirection.ASCENDING): Query
    fun limit(limit: Long): Query
    fun limitToLast(limit: Long): Query
    fun startAt(document: DocumentSnapshot): Query
    fun startAt(vararg fieldValues: Any?): Query
    fun startAfter(document: DocumentSnapshot): Query
    fun startAfter(vararg fieldValues: Any?): Query
    fun endAt(document: DocumentSnapshot): Query
    fun endAt(vararg fieldValues: Any?): Query
    fun endBefore(document: DocumentSnapshot): Query
    fun endBefore(vararg fieldValues: Any?): Query

    fun count(): AggregateQuery
    fun sum(field: String): AggregateQuery
    fun sum(field: FieldPath): AggregateQuery
    fun average(field: String): AggregateQuery
    fun average(field: FieldPath): AggregateQuery
    fun aggregate(field: AggregateField, vararg fields: AggregateField): AggregateQuery
}

fun Query.getSnapshots(cache: Boolean = true): Flow<QuerySnapshot> =
    snapshots(includeMetadataChanges = !cache, source = ListenSource.DEFAULT)
        .filter { querySnapshot -> !querySnapshot.metadata.isFromCache || cache }

fun Query.whereEqualTo(field: String, value: Any?): Query = where(field, WhereOperator.EQUAL, value)
fun Query.whereNotEqualTo(field: String, value: Any?): Query = where(field, WhereOperator.NOT_EQUAL, value)
fun Query.whereLessThan(field: String, value: Any): Query = where(field, WhereOperator.LESS_THAN, value)
fun Query.whereLessThanOrEqualTo(field: String, value: Any): Query = where(field, WhereOperator.LESS_THAN_OR_EQUAL, value)
fun Query.whereGreaterThan(field: String, value: Any): Query = where(field, WhereOperator.GREATER_THAN, value)
fun Query.whereGreaterThanOrEqualTo(field: String, value: Any): Query = where(field, WhereOperator.GREATER_THAN_OR_EQUAL, value)
fun Query.whereArrayContains(field: String, value: Any): Query = where(field, WhereOperator.ARRAY_CONTAINS, value)
fun Query.whereArrayContainsAny(field: String, values: List<Any>): Query = where(field, WhereOperator.ARRAY_CONTAINS_ANY, values)
fun Query.whereInArray(field: String, values: List<Any>): Query = where(field, WhereOperator.IN, values)
fun Query.inArray(field: String, values: List<Any>): Query = where(field, WhereOperator.IN, values)
fun Query.whereIn(field: String, values: List<Any>): Query = where(field, WhereOperator.IN, values)
fun Query.whereNotIn(field: String, values: List<Any>): Query = where(field, WhereOperator.NOT_IN, values)

fun Query.whereEqualTo(field: FieldPath, value: Any?): Query = where(field, WhereOperator.EQUAL, value)
fun Query.whereNotEqualTo(field: FieldPath, value: Any?): Query = where(field, WhereOperator.NOT_EQUAL, value)
fun Query.whereLessThan(field: FieldPath, value: Any): Query = where(field, WhereOperator.LESS_THAN, value)
fun Query.whereLessThanOrEqualTo(field: FieldPath, value: Any): Query = where(field, WhereOperator.LESS_THAN_OR_EQUAL, value)
fun Query.whereGreaterThan(field: FieldPath, value: Any): Query = where(field, WhereOperator.GREATER_THAN, value)
fun Query.whereGreaterThanOrEqualTo(field: FieldPath, value: Any): Query = where(field, WhereOperator.GREATER_THAN_OR_EQUAL, value)
fun Query.whereArrayContains(field: FieldPath, value: Any): Query = where(field, WhereOperator.ARRAY_CONTAINS, value)
fun Query.whereArrayContainsAny(field: FieldPath, values: List<Any>): Query = where(field, WhereOperator.ARRAY_CONTAINS_ANY, values)
fun Query.whereInArray(field: FieldPath, values: List<Any>): Query = where(field, WhereOperator.IN, values)
fun Query.inArray(field: FieldPath, values: List<Any>): Query = where(field, WhereOperator.IN, values)
fun Query.whereIn(field: FieldPath, values: List<Any>): Query = where(field, WhereOperator.IN, values)
fun Query.whereNotIn(field: FieldPath, values: List<Any>): Query = where(field, WhereOperator.NOT_IN, values)


fun Query.snapshots(includeMetadataChanges: Boolean): Flow<QuerySnapshot> =
    snapshots(includeMetadataChanges = includeMetadataChanges, source = ListenSource.DEFAULT)

fun Query.snapshots(source: ListenSource): Flow<QuerySnapshot> =
    snapshots(includeMetadataChanges = false, source = source)


class QueryFilterBuilder(val query: Query) {
    infix fun FieldPath.inArray(values: List<Any>): Query = query.whereInArray(this, values)
    infix fun String.inArray(values: List<Any>): Query = query.whereInArray(this, values)
    infix fun FieldPath.equalTo(value: Any?): Query = query.whereEqualTo(this, value)
    infix fun String.equalTo(value: Any?): Query = query.whereEqualTo(this, value)
    infix fun FieldPath.notEqualTo(value: Any?): Query = query.whereNotEqualTo(this, value)
    infix fun String.notEqualTo(value: Any?): Query = query.whereNotEqualTo(this, value)
    infix fun FieldPath.lessThan(value: Any): Query = query.whereLessThan(this, value)
    infix fun String.lessThan(value: Any): Query = query.whereLessThan(this, value)
    infix fun FieldPath.lessThanOrEqualTo(value: Any): Query = query.whereLessThanOrEqualTo(this, value)
    infix fun String.lessThanOrEqualTo(value: Any): Query = query.whereLessThanOrEqualTo(this, value)
    infix fun FieldPath.greaterThan(value: Any): Query = query.whereGreaterThan(this, value)
    infix fun String.greaterThan(value: Any): Query = query.whereGreaterThan(this, value)
    infix fun FieldPath.greaterThanOrEqualTo(value: Any): Query = query.whereGreaterThanOrEqualTo(this, value)
    infix fun String.greaterThanOrEqualTo(value: Any): Query = query.whereGreaterThanOrEqualTo(this, value)
    infix fun FieldPath.arrayContains(value: Any): Query = query.whereArrayContains(this, value)
    infix fun String.arrayContains(value: Any): Query = query.whereArrayContains(this, value)
    infix fun FieldPath.arrayContainsAny(values: List<Any>): Query = query.whereArrayContainsAny(this, values)
    infix fun String.arrayContainsAny(values: List<Any>): Query = query.whereArrayContainsAny(this, values)
    infix fun FieldPath.whereIn(values: List<Any>): Query = query.whereIn(this, values)
    infix fun String.whereIn(values: List<Any>): Query = query.whereIn(this, values)
    infix fun FieldPath.whereNotIn(values: List<Any>): Query = query.whereNotIn(this, values)
    infix fun String.whereNotIn(values: List<Any>): Query = query.whereNotIn(this, values)
}

fun Query.where(block: QueryFilterBuilder.() -> Query): Query = QueryFilterBuilder(this).block()

val Query.snapshots: Flow<QuerySnapshot>
    get() = snapshots
