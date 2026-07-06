package zone.ien.firebase.storage

import com.google.firebase.storage.UploadTask as AndroidUploadTask
import com.google.firebase.storage.OnProgressListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

public actual class UploadTask(private val androidTask: AndroidUploadTask) {
    public actual suspend fun await() {
        androidTask.await()
    }

    public actual fun snapshots(): Flow<UploadTaskSnapshot> = callbackFlow {
        val listener = OnProgressListener<AndroidUploadTask.TaskSnapshot> { snapshot ->
            trySend(UploadTaskSnapshot(snapshot))
        }
        androidTask.addOnProgressListener(listener)
        awaitClose {
            androidTask.removeOnProgressListener(listener)
        }
    }
}

public actual class UploadTaskSnapshot(private val androidSnapshot: AndroidUploadTask.TaskSnapshot) {
    public actual val bytesTransferred: Long
        get() = androidSnapshot.bytesTransferred

    public actual val totalByteCount: Long
        get() = androidSnapshot.totalByteCount
}
