package zone.ien.firebase.appcheck

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            cont.resume(task.result)
        } else if (task.isCanceled) {
            cont.cancel()
        } else {
            cont.resumeWithException(task.exception ?: RuntimeException("Task failed"))
        }
    }
}
