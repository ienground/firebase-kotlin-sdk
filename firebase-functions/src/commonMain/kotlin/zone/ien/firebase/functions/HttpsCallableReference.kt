package zone.ien.firebase.functions

expect class HttpsCallableReference {
    suspend fun call(): HttpsCallableResult
    suspend fun call(data: Any?): HttpsCallableResult
    suspend operator fun invoke(data: Any? = null): HttpsCallableResult
}

suspend operator fun HttpsCallableReference.invoke(data: Any? = null): HttpsCallableResult =
    call(data)
