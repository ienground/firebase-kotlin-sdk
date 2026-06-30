package zone.ien.firebase.functions

import com.google.firebase.functions.HttpsCallableReference as AndroidHttpsCallableReference

actual class HttpsCallableReference(private val androidReference: AndroidHttpsCallableReference) {
    actual suspend fun call(): HttpsCallableResult {
        return try {
            val result = androidReference.call().await()
            HttpsCallableResult(result)
        } catch (e: com.google.firebase.functions.FirebaseFunctionsException) {
            throw FirebaseFunctionsException(e)
        }
    }

    actual suspend fun call(data: Any?): HttpsCallableResult {
        return try {
            val result = androidReference.call(data).await()
            HttpsCallableResult(result)
        } catch (e: com.google.firebase.functions.FirebaseFunctionsException) {
            throw FirebaseFunctionsException(e)
        }
    }
}
