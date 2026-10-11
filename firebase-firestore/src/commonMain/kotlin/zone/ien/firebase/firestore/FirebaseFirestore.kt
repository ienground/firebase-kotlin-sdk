package zone.ien.firebase.firestore

import zone.ien.firebase.Firebase

expect class FirebaseFirestore {
    fun collection(collectionPath: String): CollectionReference
    fun collectionGroup(collectionId: String): Query
    fun document(documentPath: String): DocumentReference
    fun batch(): WriteBatch
    suspend fun <T> runTransaction(block: (Transaction) -> T): T
    fun setSettings(settings: FirebaseFirestoreSettings)
    suspend fun clearPersistence()
    suspend fun enableNetwork()
    suspend fun disableNetwork()
    suspend fun waitForPendingWrites()
    suspend fun loadBundle(bundleData: ByteArray): LoadBundleTaskProgress
    suspend fun namedQuery(name: String): Query?

    companion object {
        fun getInstance(): FirebaseFirestore
    }
}

val Firebase.firestore: FirebaseFirestore
    get() = FirebaseFirestore.getInstance()
