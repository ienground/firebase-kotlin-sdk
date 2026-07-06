package zone.ien.firebase.firestore

import com.google.firebase.firestore.DocumentSnapshot as AndroidDocumentSnapshot
import com.google.firebase.firestore.Query as AndroidQuery
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual open class Query(private val androidQuery: AndroidQuery) {
    actual suspend fun get(): QuerySnapshot {
        return QuerySnapshot(androidQuery.get().await())
    }

    actual fun snapshots(): Flow<QuerySnapshot> = callbackFlow {
        val listener = androidQuery.addSnapshotListener { snapshot, error ->
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
            WhereOperator.EQUAL -> androidQuery.whereEqualTo(field, value)
            WhereOperator.NOT_EQUAL -> androidQuery.whereNotEqualTo(field, value)
            WhereOperator.LESS_THAN -> androidQuery.whereLessThan(field, value)
            WhereOperator.LESS_THAN_OR_EQUAL -> androidQuery.whereLessThanOrEqualTo(field, value)
            WhereOperator.GREATER_THAN -> androidQuery.whereGreaterThan(field, value)
            WhereOperator.GREATER_THAN_OR_EQUAL -> androidQuery.whereGreaterThanOrEqualTo(field, value)
            WhereOperator.ARRAY_CONTAINS -> androidQuery.whereArrayContains(field, value)
            WhereOperator.ARRAY_CONTAINS_ANY -> androidQuery.whereArrayContainsAny(field, value.asList())
            WhereOperator.IN -> androidQuery.whereIn(field, value.asList())
            WhereOperator.NOT_IN -> androidQuery.whereNotIn(field, value.asList())
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

    private fun Any.asList(): List<Any> {
        return this as? List<Any>
            ?: throw IllegalArgumentException("Firestore query operator requires a List value.")
    }
}
