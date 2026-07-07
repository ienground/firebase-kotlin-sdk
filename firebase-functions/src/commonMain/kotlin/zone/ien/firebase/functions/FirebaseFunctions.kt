package zone.ien.firebase.functions

import zone.ien.firebase.FirebaseApp

expect class FirebaseFunctions {
    fun getHttpsCallable(name: String): HttpsCallableReference
    fun getHttpsCallableFromUrl(url: String): HttpsCallableReference
    fun useEmulator(host: String, port: Int)

    companion object {
        fun getInstance(): FirebaseFunctions
        fun getInstance(app: FirebaseApp): FirebaseFunctions
        fun getInstance(region: String): FirebaseFunctions
        fun getInstance(app: FirebaseApp, region: String): FirebaseFunctions
    }
}
