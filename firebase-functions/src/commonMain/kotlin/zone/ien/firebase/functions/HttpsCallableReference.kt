package zone.ien.firebase.functions

expect class HttpsCallableReference {
    suspend fun call(): HttpsCallableResult
    suspend fun call(data: Any?): HttpsCallableResult
}
