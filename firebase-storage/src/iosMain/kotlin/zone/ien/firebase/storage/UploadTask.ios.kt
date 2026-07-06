package zone.ien.firebase.storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorageUploadTask
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorageTaskSnapshot
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorageTaskStatusProgress
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorageTaskStatusSuccess
import swiftPMImport.zone.ien.firebase.firebase.storage.FIRStorageTaskStatusFailure

@OptIn(ExperimentalForeignApi::class)
public actual class UploadTask(private val iosTask: FIRStorageUploadTask) {
    public actual suspend fun await(): Unit = suspendCancellableCoroutine { cont ->
        var successHandle: String? = null
        var failureHandle: String? = null

        val cleanup = {
            successHandle?.let { iosTask.removeObserverWithHandle(it) }
            failureHandle?.let { iosTask.removeObserverWithHandle(it) }
        }

        successHandle = iosTask.observeStatus(FIRStorageTaskStatusSuccess) { _ ->
            cleanup()
            cont.resume(Unit)
        }

        failureHandle = iosTask.observeStatus(FIRStorageTaskStatusFailure) { snapshot ->
            cleanup()
            val error = snapshot?.error()
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resumeWithException(RuntimeException("Upload task failed with unknown error"))
            }
        }

        cont.invokeOnCancellation {
            cleanup()
        }
    }

    public actual fun snapshots(): Flow<UploadTaskSnapshot> = callbackFlow {
        val handle = iosTask.observeStatus(FIRStorageTaskStatusProgress) { snapshot ->
            if (snapshot != null) {
                trySend(UploadTaskSnapshot(snapshot))
            }
        }
        awaitClose {
            iosTask.removeObserverWithHandle(handle)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
public actual class UploadTaskSnapshot(private val iosSnapshot: FIRStorageTaskSnapshot) {
    public actual val bytesTransferred: Long
        get() = iosSnapshot.progress()?.completedUnitCount ?: 0L

    public actual val totalByteCount: Long
        get() = iosSnapshot.progress()?.totalUnitCount ?: 0L
}
