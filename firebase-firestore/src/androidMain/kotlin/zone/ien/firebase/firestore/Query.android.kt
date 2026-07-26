package zone.ien.firebase.firestore

import com.google.firebase.firestore.AggregateField as AndroidAggregateField
import com.google.firebase.firestore.DocumentSnapshot as AndroidDocumentSnapshot
import com.google.firebase.firestore.FieldPath as AndroidFieldPath
import com.google.firebase.firestore.Query as AndroidQuery
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual open class Query(private val androidQuery: AndroidQuery) {
    actual suspend fun get(): QuerySnapshot = get(Source.DEFAULT)

    actual suspend fun get(source: Source): QuerySnapshot {
        return QuerySnapshot(androidQuery.get(source.toAndroidSource()).await())
    }

    actual fun snapshots(): Flow<QuerySnapshot> = snapshots(
        includeMetadataChanges = false,
        source = ListenSource.DEFAULT
    )

    actual fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<QuerySnapshot> = callbackFlow {
        val options = snapshotListenOptions(includeMetadataChanges, source)
        val listener = androidQuery.addSnapshotListener(options) { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                trySend(QuerySnapshot(snapshot))
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    actual fun where(field: String, operator: WhereOperator, value: Any): Query {
        val filteredQuery = when (operator) {
            WhereOperator.EQUAL -> androidQuery.whereEqualTo(field, value.toAndroidValue())
            WhereOperator.NOT_EQUAL -> androidQuery.whereNotEqualTo(field, value.toAndroidValue())
            WhereOperator.LESS_THAN -> androidQuery.whereLessThan(field, value.toAndroidValue())
            WhereOperator.LESS_THAN_OR_EQUAL ->
                androidQuery.whereLessThanOrEqualTo(field, value.toAndroidValue())
            WhereOperator.GREATER_THAN -> androidQuery.whereGreaterThan(field, value.toAndroidValue())
            WhereOperator.GREATER_THAN_OR_EQUAL ->
                androidQuery.whereGreaterThanOrEqualTo(field, value.toAndroidValue())
            WhereOperator.ARRAY_CONTAINS ->
                androidQuery.whereArrayContains(field, value.toAndroidValue())
            WhereOperator.ARRAY_CONTAINS_ANY ->
                androidQuery.whereArrayContainsAny(field, value.asNativeList())
            WhereOperator.IN -> androidQuery.whereIn(field, value.asNativeList())
            WhereOperator.NOT_IN -> androidQuery.whereNotIn(field, value.asNativeList())
        }
        return Query(filteredQuery)
    }

    actual fun orderBy(field: String, direction: QueryDirection): Query {
        val androidDirection = when (direction) {
            QueryDirection.ASCENDING -> AndroidQuery.Direction.ASCENDING
            QueryDirection.DESCENDING -> AndroidQuery.Direction.DESCENDING
        }
        return Query(androidQuery.orderBy(field, androidDirection))
    }

    actual fun where(field: FieldPath, operator: WhereOperator, value: Any): Query {
        val fp = field.nativePath() as AndroidFieldPath
        val filteredQuery = when (operator) {
            WhereOperator.EQUAL -> androidQuery.whereEqualTo(fp, value.toAndroidValue())
            WhereOperator.NOT_EQUAL -> androidQuery.whereNotEqualTo(fp, value.toAndroidValue())
            WhereOperator.LESS_THAN -> androidQuery.whereLessThan(fp, value.toAndroidValue())
            WhereOperator.LESS_THAN_OR_EQUAL ->
                androidQuery.whereLessThanOrEqualTo(fp, value.toAndroidValue())
            WhereOperator.GREATER_THAN -> androidQuery.whereGreaterThan(fp, value.toAndroidValue())
            WhereOperator.GREATER_THAN_OR_EQUAL ->
                androidQuery.whereGreaterThanOrEqualTo(fp, value.toAndroidValue())
            WhereOperator.ARRAY_CONTAINS ->
                androidQuery.whereArrayContains(fp, value.toAndroidValue())
            WhereOperator.ARRAY_CONTAINS_ANY ->
                androidQuery.whereArrayContainsAny(fp, value.asNativeList())
            WhereOperator.IN -> androidQuery.whereIn(fp, value.asNativeList())
            WhereOperator.NOT_IN -> androidQuery.whereNotIn(fp, value.asNativeList())
        }
        return Query(filteredQuery)
    }

    actual fun orderBy(field: FieldPath, direction: QueryDirection): Query {
        val fp = field.nativePath() as AndroidFieldPath
        val androidDirection = when (direction) {
            QueryDirection.ASCENDING -> AndroidQuery.Direction.ASCENDING
            QueryDirection.DESCENDING -> AndroidQuery.Direction.DESCENDING
        }
        return Query(androidQuery.orderBy(fp, androidDirection))
    }

    actual fun limit(limit: Long): Query = Query(androidQuery.limit(limit))
    actual fun limitToLast(limit: Long): Query = Query(androidQuery.limitToLast(limit))
    actual fun startAt(document: DocumentSnapshot): Query =
        Query(androidQuery.startAt(document.nativeSnapshot() as AndroidDocumentSnapshot))
    actual fun startAfter(document: DocumentSnapshot): Query =
        Query(androidQuery.startAfter(document.nativeSnapshot() as AndroidDocumentSnapshot))
    actual fun endAt(document: DocumentSnapshot): Query =
        Query(androidQuery.endAt(document.nativeSnapshot() as AndroidDocumentSnapshot))
    actual fun endBefore(document: DocumentSnapshot): Query =
        Query(androidQuery.endBefore(document.nativeSnapshot() as AndroidDocumentSnapshot))
    actual fun count(): AggregateQuery = AggregateQuery(androidQuery.count())
    actual fun sum(field: String): AggregateQuery =
        AggregateQuery(androidQuery.aggregate(AndroidAggregateField.sum(field)))
    actual fun sum(field: FieldPath): AggregateQuery =
        AggregateQuery(androidQuery.aggregate(AndroidAggregateField.sum(field.nativePath() as AndroidFieldPath)))
    actual fun average(field: String): AggregateQuery =
        AggregateQuery(androidQuery.aggregate(AndroidAggregateField.average(field)))
    actual fun average(field: FieldPath): AggregateQuery =
        AggregateQuery(androidQuery.aggregate(AndroidAggregateField.average(field.nativePath() as AndroidFieldPath)))
    actual fun aggregate(field: AggregateField, vararg fields: AggregateField): AggregateQuery {
        val rest = fields.map { it.androidField }.toTypedArray()
        return AggregateQuery(androidQuery.aggregate(field.androidField, *rest))
    }

    private fun Any.asNativeList(): List<Any> =
        (this as? List<*>)
            ?.map {
                requireNotNull(it) {
                    "Firestore query values must not contain null."
                }.toAndroidValue()
            }
            ?: throw IllegalArgumentException("Firestore query operator requires a List value.")
}
