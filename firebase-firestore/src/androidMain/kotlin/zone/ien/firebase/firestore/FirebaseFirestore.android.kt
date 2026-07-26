package zone.ien.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore as AndroidFirebaseFirestore
import com.google.firebase.firestore.LoadBundleTaskProgress as AndroidLoadBundleTaskProgress

actual class FirebaseFirestore(internal val androidFirestore: AndroidFirebaseFirestore) {
    actual fun collection(collectionPath: String): CollectionReference {
        return CollectionReference(androidFirestore.collection(collectionPath))
    }

    actual fun document(documentPath: String): DocumentReference {
        return DocumentReference(androidFirestore.document(documentPath))
    }

    actual fun batch(): WriteBatch = WriteBatch(androidFirestore.batch())

    actual suspend fun <T> runTransaction(block: (Transaction) -> T): T {
        return androidFirestore.runTransaction { androidTransaction ->
            block(Transaction(androidTransaction))
        }.await()
    }

    actual fun setSettings(settings: FirebaseFirestoreSettings) {
        androidFirestore.firestoreSettings = settings.androidSettings
    }

    actual suspend fun clearPersistence() {
        androidFirestore.clearPersistence().await()
    }

    actual suspend fun loadBundle(bundleData: ByteArray): LoadBundleTaskProgress {
        val androidProgress = androidFirestore.loadBundle(bundleData).await()
        val state = when (androidProgress.taskState) {
            AndroidLoadBundleTaskProgress.TaskState.ERROR -> LoadBundleTaskState.ERROR
            AndroidLoadBundleTaskProgress.TaskState.RUNNING -> LoadBundleTaskState.RUNNING
            AndroidLoadBundleTaskProgress.TaskState.SUCCESS -> LoadBundleTaskState.SUCCESS
        }
        return LoadBundleTaskProgress(
            documentsLoaded = androidProgress.documentsLoaded,
            totalDocuments = androidProgress.totalDocuments,
            bytesLoaded = androidProgress.bytesLoaded,
            totalBytes = androidProgress.totalBytes,
            taskState = state
        )
    }

    actual suspend fun namedQuery(name: String): Query? {
        val androidQuery = androidFirestore.getNamedQuery(name).await()
        return androidQuery?.let { Query(it) }
    }

    actual companion object {
        actual fun getInstance(): FirebaseFirestore {
            return FirebaseFirestore(AndroidFirebaseFirestore.getInstance())
        }
    }
}
