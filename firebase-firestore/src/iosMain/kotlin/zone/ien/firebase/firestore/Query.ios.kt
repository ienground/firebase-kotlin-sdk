package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentSnapshot
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRQuery

@OptIn(ExperimentalForeignApi::class)
actual open class Query(private val iosQuery: FIRQuery) {
    actual suspend fun get(): QuerySnapshot = suspendCancellableCoroutine { cont ->
        iosQuery.getDocumentsWithCompletion { snapshot, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (snapshot != null) {
                cont.resume(QuerySnapshot(snapshot))
            } else {
                cont.resumeWithException(RuntimeException("Snapshot was null"))
            }
        }
    }

    actual fun snapshots(): Flow<QuerySnapshot> = callbackFlow {
        val handle = iosQuery.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(RuntimeException(error.localizedDescription))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                trySend(QuerySnapshot(snapshot))
            }
        }
        awaitClose {
            handle.remove()
        }
    }

    actual fun where(field: String, operator: WhereOperator, value: Any): Query {
        val filteredQuery = when (operator) {
            WhereOperator.EQUAL -> iosQuery.queryWhereField(field, isEqualTo = value)
            WhereOperator.NOT_EQUAL -> iosQuery.queryWhereField(field, isNotEqualTo = value)
            WhereOperator.LESS_THAN -> iosQuery.queryWhereField(field, isLessThan = value)
            WhereOperator.LESS_THAN_OR_EQUAL -> iosQuery.queryWhereField(field, isLessThanOrEqualTo = value)
            WhereOperator.GREATER_THAN -> iosQuery.queryWhereField(field, isGreaterThan = value)
            WhereOperator.GREATER_THAN_OR_EQUAL -> iosQuery.queryWhereField(field, isGreaterThanOrEqualTo = value)
            WhereOperator.ARRAY_CONTAINS -> iosQuery.queryWhereField(field, arrayContains = value)
            WhereOperator.ARRAY_CONTAINS_ANY -> iosQuery.queryWhereField(field, arrayContainsAny = value.asList())
            WhereOperator.IN -> iosQuery.queryWhereField(field, `in` = value.asList())
            WhereOperator.NOT_IN -> iosQuery.queryWhereField(field, notIn = value.asList())
        }
        return Query(filteredQuery)
    }

    actual fun orderBy(field: String, direction: QueryDirection): Query {
        return Query(
            iosQuery.queryOrderedByField(
                field = field,
                descending = direction == QueryDirection.DESCENDING
            )
        )
    }

    actual fun limit(limit: Long): Query = Query(iosQuery.queryLimitedTo(limit.toLong()))

    actual fun limitToLast(limit: Long): Query = Query(iosQuery.queryLimitedToLast(limit.toLong()))

    actual fun startAt(document: DocumentSnapshot): Query =
        Query(iosQuery.queryStartingAtDocument(document.nativeSnapshot() as FIRDocumentSnapshot))

    actual fun startAfter(document: DocumentSnapshot): Query =
        Query(iosQuery.queryStartingAfterDocument(document.nativeSnapshot() as FIRDocumentSnapshot))

    actual fun endAt(document: DocumentSnapshot): Query =
        Query(iosQuery.queryEndingAtDocument(document.nativeSnapshot() as FIRDocumentSnapshot))

    actual fun endBefore(document: DocumentSnapshot): Query =
        Query(iosQuery.queryEndingBeforeDocument(document.nativeSnapshot() as FIRDocumentSnapshot))

    private fun Any.asList(): List<Any> {
        return this as? List<Any>
            ?: throw IllegalArgumentException("Firestore query operator requires a List value.")
    }
}
