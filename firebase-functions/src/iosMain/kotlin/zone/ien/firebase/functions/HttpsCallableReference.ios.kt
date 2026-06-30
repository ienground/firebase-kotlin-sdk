package zone.ien.firebase.functions

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import swiftPMImport.zone.ien.firebase.firebase.functions.FIRHTTPSCallable
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
actual class HttpsCallableReference(private val iosCallable: FIRHTTPSCallable) {
    actual suspend fun call(): HttpsCallableResult = suspendCancellableCoroutine { cont ->
        iosCallable.callWithCompletion { result, error ->
            if (error != null) {
                cont.resumeWithException(mapIosException(error))
            } else if (result != null) {
                cont.resume(HttpsCallableResult(result))
            } else {
                cont.resumeWithException(RuntimeException("Functions result is null"))
            }
        }
    }

    actual suspend fun call(data: Any?): HttpsCallableResult = suspendCancellableCoroutine { cont ->
        iosCallable.callWithObject(data) { result, error ->
            if (error != null) {
                cont.resumeWithException(mapIosException(error))
            } else if (result != null) {
                cont.resume(HttpsCallableResult(result))
            } else {
                cont.resumeWithException(RuntimeException("Functions result is null"))
            }
        }
    }
}

private fun mapIosException(error: NSError): FirebaseFunctionsException {
    return FirebaseFunctionsException(error)
}
