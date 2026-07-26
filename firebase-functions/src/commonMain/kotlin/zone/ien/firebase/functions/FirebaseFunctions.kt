package zone.ien.firebase.functions

import zone.ien.firebase.Firebase
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

val Firebase.functions: FirebaseFunctions
    get() = FirebaseFunctions.getInstance()

fun Firebase.functions(app: FirebaseApp): FirebaseFunctions =
    FirebaseFunctions.getInstance(app)

fun Firebase.functions(region: String): FirebaseFunctions =
    FirebaseFunctions.getInstance(region)

fun Firebase.functions(app: FirebaseApp, region: String): FirebaseFunctions =
    FirebaseFunctions.getInstance(app, region)
