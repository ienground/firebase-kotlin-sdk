package zone.ien.firebase.firestore

import com.google.firebase.firestore.DocumentReference as AndroidDocumentReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class DocumentReference(internal val androidDocument: AndroidDocumentReference) {
    actual fun getId(): String = androidDocument.id
    actual fun getPath(): String = androidDocument.path

    actual suspend fun set(data: Map<String, Any>) {
        androidDocument.set(data.toAndroidData()).await()
    }

    actual suspend fun update(data: Map<String, Any>) {
        androidDocument.update(data.toAndroidData()).await()
    }

    actual suspend fun delete() {
        androidDocument.delete().await()
    }

    actual suspend fun get(): DocumentSnapshot = get(Source.DEFAULT)

    actual suspend fun get(source: Source): DocumentSnapshot {
        return DocumentSnapshot(androidDocument.get(source.toAndroidSource()).await())
    }

    actual fun snapshots(): Flow<DocumentSnapshot?> = snapshots(
        includeMetadataChanges = false,
        source = ListenSource.DEFAULT
    )

    actual fun snapshots(
        includeMetadataChanges: Boolean,
        source: ListenSource
    ): Flow<DocumentSnapshot?> = callbackFlow {
        val options = snapshotListenOptions(includeMetadataChanges, source)
        val listener = androidDocument.addSnapshotListener(options) { snapshot, error ->
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
