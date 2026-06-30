package zone.ien.firebase.firestore

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
}
