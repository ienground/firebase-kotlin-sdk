package zone.ien.firebase.functions

import com.google.firebase.functions.HttpsCallableResult as AndroidHttpsCallableResult

actual class HttpsCallableResult(private val androidResult: AndroidHttpsCallableResult) {
    actual val data: Any?
        get() = androidResult.data
}
