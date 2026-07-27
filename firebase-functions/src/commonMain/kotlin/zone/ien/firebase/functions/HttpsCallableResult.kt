package zone.ien.firebase.functions

expect class HttpsCallableResult {
    val data: Any?
    fun <T> data(): T
}

@Suppress("UNCHECKED_CAST")
fun <T> HttpsCallableResult.data(): T =
    data as T
