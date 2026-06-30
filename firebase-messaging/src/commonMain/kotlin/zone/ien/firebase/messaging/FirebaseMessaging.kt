package zone.ien.firebase.messaging

import zone.ien.firebase.FirebaseApp

expect class FirebaseMessaging {
    suspend fun getToken(): String
    suspend fun deleteToken()

    fun addOnMessageReceivedListener(listener: (Map<String, String>) -> Unit)

    companion object {
        fun getInstance(): FirebaseMessaging
        fun getInstance(app: FirebaseApp): FirebaseMessaging
    }
}
