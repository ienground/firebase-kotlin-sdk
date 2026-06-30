package zone.ien.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRDocumentReference

@OptIn(ExperimentalForeignApi::class)
actual class DocumentReference(private val iosDocument: FIRDocumentReference) {
    actual fun getId(): String = iosDocument.documentID()
    actual fun getPath(): String = iosDocument.path()

    actual suspend fun set(data: Map<String, Any>) = suspendCancellableCoroutine<Unit> { cont ->
        iosDocument.setData(data as Map<Any?, *>) { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun update(data: Map<String, Any>) = suspendCancellableCoroutine<Unit> { cont ->
        iosDocument.updateData(data as Map<Any?, *>) { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun delete() = suspendCancellableCoroutine<Unit> { cont ->
        iosDocument.deleteDocumentWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    actual suspend fun get(): DocumentSnapshot = suspendCancellableCoroutine { cont ->
        iosDocument.getDocumentWithCompletion { snapshot, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (snapshot != null) {
                cont.resume(DocumentSnapshot(snapshot))
            } else {
                cont.resumeWithException(RuntimeException("Snapshot was null"))
            }
        }
    }

    actual fun snapshots(): Flow<DocumentSnapshot?> = callbackFlow {
        val handle = iosDocument.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(RuntimeException(error.localizedDescription))
                return@addSnapshotListener
            }
            trySend(snapshot?.let { DocumentSnapshot(it) })
        }
        awaitClose {
            handle.remove()
        }
    }
}
