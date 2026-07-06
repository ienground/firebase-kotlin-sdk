package zone.ien.firebase.database

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.database.FIRDatabaseQuery
import swiftPMImport.zone.ien.firebase.firebase.database.FIRDataEventType

@OptIn(ExperimentalForeignApi::class)
public actual open class Query(private val iosQuery: FIRDatabaseQuery) {
    public actual suspend fun get(): DataSnapshot = suspendCancellableCoroutine { cont ->
        iosQuery.observeSingleEventOfType(
            FIRDataEventType.FIRDataEventTypeValue,
            withBlock = { snapshot ->
                if (snapshot != null) {
                    cont.resume(DataSnapshot(snapshot))
                } else {
                    cont.resumeWithException(DatabaseException("Snapshot was null", null))
                }
            },
            withCancelBlock = { error ->
                if (error != null) {
                    cont.resumeWithException(DatabaseException(error.localizedDescription, null))
                } else {
                    cont.resumeWithException(DatabaseException("Unknown cancel error", null))
                }
            }
        )
    }

    public actual fun snapshots(): Flow<DataSnapshot> = callbackFlow {
        val handle = iosQuery.observeEventType(
            FIRDataEventType.FIRDataEventTypeValue,
            withBlock = { snapshot ->
                if (snapshot != null) {
                    trySend(DataSnapshot(snapshot))
                }
            },
            withCancelBlock = { error ->
                if (error != null) {
                    close(DatabaseException(error.localizedDescription, null))
                } else {
                    close(DatabaseException("Unknown cancel error", null))
                }
            }
        )
        awaitClose {
            iosQuery.removeObserverWithHandle(handle)
        }
    }

    public actual fun orderByChild(path: String): Query = Query(iosQuery.queryOrderedByChild(path))
    public actual fun orderByKey(): Query = Query(iosQuery.queryOrderedByKey())
    public actual fun orderByValue(): Query = Query(iosQuery.queryOrderedByValue())
    public actual fun limitToFirst(limit: Int): Query {
        require(limit > 0) { "Limit must be a positive integer" }
        return Query(iosQuery.queryLimitedToFirst(limit.toULong()))
    }
    public actual fun limitToLast(limit: Int): Query {
        require(limit > 0) { "Limit must be a positive integer" }
        return Query(iosQuery.queryLimitedToLast(limit.toULong()))
    }
    public actual fun equalTo(value: String): Query = Query(iosQuery.queryEqualToValue(value))
    public actual fun equalTo(value: Double): Query = Query(iosQuery.queryEqualToValue(value))
    public actual fun equalTo(value: Boolean): Query = Query(iosQuery.queryEqualToValue(value))
}
