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
        var isCompleted = false

        val cleanup = {
            successHandle?.let { iosTask.removeObserverWithHandle(it) }
            failureHandle?.let { iosTask.removeObserverWithHandle(it) }
        }

        val sHandle = iosTask.observeStatus(FIRStorageTaskStatusSuccess) { _ ->
            isCompleted = true
            cleanup()
            if (cont.isActive) cont.resume(Unit)
        }
        successHandle = sHandle

        val fHandle = iosTask.observeStatus(FIRStorageTaskStatusFailure) { snapshot ->
            isCompleted = true
            cleanup()
            if (cont.isActive) {
                val error = snapshot?.error()
                if (error != null) {
                    cont.resumeWithException(RuntimeException(error.localizedDescription))
                } else {
                    cont.resumeWithException(RuntimeException("Upload task failed with unknown error"))
                }
            }
        }
        failureHandle = fHandle

        if (isCompleted) {
            cleanup()
        }

        cont.invokeOnCancellation {
            cleanup()
        }
    }

    public actual fun snapshots(): Flow<UploadTaskSnapshot> = callbackFlow {
        var progressHandle: String? = null
        var successHandle: String? = null
        var failureHandle: String? = null
        var isCompleted = false

        val cleanup = {
            progressHandle?.let { iosTask.removeObserverWithHandle(it) }
            successHandle?.let { iosTask.removeObserverWithHandle(it) }
            failureHandle?.let { iosTask.removeObserverWithHandle(it) }
        }

        val pHandle = iosTask.observeStatus(FIRStorageTaskStatusProgress) { snapshot ->
            if (snapshot != null) {
                trySend(UploadTaskSnapshot(snapshot))
            }
        }
        progressHandle = pHandle

        val sHandle = iosTask.observeStatus(FIRStorageTaskStatusSuccess) { snapshot ->
            if (snapshot != null) {
                trySend(UploadTaskSnapshot(snapshot))
            }
            isCompleted = true
            cleanup()
            close()
        }
        successHandle = sHandle

        val fHandle = iosTask.observeStatus(FIRStorageTaskStatusFailure) { snapshot ->
            isCompleted = true
            cleanup()
            val error = snapshot?.error()
            if (error != null) {
                close(RuntimeException(error.localizedDescription))
            } else {
                close(RuntimeException("Upload task failed with unknown error"))
            }
        }
        failureHandle = fHandle

        if (isCompleted) {
            cleanup()
        }

        awaitClose {
            cleanup()
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
