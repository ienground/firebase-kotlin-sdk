package zone.ien.firebase.functions

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T {
    if (isComplete) {
        val e = exception
        if (e == null) {
            if (isCanceled) {
                throw kotlinx.coroutines.CancellationException("Task $this was cancelled")
            }
            @Suppress("UNCHECKED_CAST")
            return result as T
        } else {
            throw e
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener { task ->
            val e = task.exception
            if (e == null) {
                if (task.isCanceled) {
                    cont.cancel()
                } else {
                    @Suppress("UNCHECKED_CAST")
                    cont.resume(task.result as T)
                }
            } else {
                cont.resumeWithException(e)
            }
        }
    }
}