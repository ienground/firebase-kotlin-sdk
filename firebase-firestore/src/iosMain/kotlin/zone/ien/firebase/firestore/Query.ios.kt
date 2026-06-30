package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
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
}
