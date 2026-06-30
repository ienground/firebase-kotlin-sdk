package zone.ien.firebase.firestore

import com.google.firebase.firestore.DocumentReference as AndroidDocumentReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class DocumentReference(private val androidDocument: AndroidDocumentReference) {
    actual fun getId(): String = androidDocument.id
    actual fun getPath(): String = androidDocument.path

    actual suspend fun set(data: Map<String, Any>) {
        androidDocument.set(data).await()
    }

    actual suspend fun update(data: Map<String, Any>) {
        androidDocument.update(data).await()
    }

    actual suspend fun delete() {
        androidDocument.delete().await()
    }

    actual suspend fun get(): DocumentSnapshot {
        return DocumentSnapshot(androidDocument.get().await())
    }

    actual fun snapshots(): Flow<DocumentSnapshot?> = callbackFlow {
        val listener = androidDocument.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.let { DocumentSnapshot(it) })
        }
        awaitClose {
            listener.remove()
        }
    }
}
