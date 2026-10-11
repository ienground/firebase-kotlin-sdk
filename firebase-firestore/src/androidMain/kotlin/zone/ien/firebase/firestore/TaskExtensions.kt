package zone.ien.firebase.firestore

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task ->
        val exception = task.exception
        if (task.isSuccessful) {
            cont.resume(task.result)
        } else {
            cont.resumeWithException(exception ?: RuntimeException("Task failed"))
        }
    }
}
