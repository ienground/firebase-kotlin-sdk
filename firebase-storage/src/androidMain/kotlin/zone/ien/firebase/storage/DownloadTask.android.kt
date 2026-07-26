package zone.ien.firebase.storage

import com.google.firebase.storage.FileDownloadTask as AndroidFileDownloadTask
import com.google.firebase.storage.OnProgressListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

public actual class DownloadTask(private val androidTask: AndroidFileDownloadTask) {
    public actual suspend fun await() {
        androidTask.await()
    }

    public actual fun snapshots(): Flow<DownloadTaskSnapshot> = callbackFlow {
        val progressListener = OnProgressListener<AndroidFileDownloadTask.TaskSnapshot> { snapshot ->
            trySend(DownloadTaskSnapshot(snapshot))
        }
        val successListener = com.google.android.gms.tasks.OnSuccessListener<AndroidFileDownloadTask.TaskSnapshot> { snapshot ->
            trySend(DownloadTaskSnapshot(snapshot))
            close()
        }
        val failureListener = com.google.android.gms.tasks.OnFailureListener { exception ->
            close(exception)
        }

        androidTask.addOnProgressListener(progressListener)
        androidTask.addOnSuccessListener(successListener)
        androidTask.addOnFailureListener(failureListener)

        awaitClose {
            androidTask.removeOnProgressListener(progressListener)
            androidTask.removeOnSuccessListener(successListener)
            androidTask.removeOnFailureListener(failureListener)
        }
    }
}

public actual class DownloadTaskSnapshot(private val androidSnapshot: AndroidFileDownloadTask.TaskSnapshot) {
    public actual val bytesTransferred: Long
        get() = androidSnapshot.bytesTransferred

    public actual val totalByteCount: Long
        get() = androidSnapshot.totalByteCount
}
