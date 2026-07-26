package zone.ien.firebase.firestore

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.create
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRFirestore
import swiftPMImport.zone.ien.firebase.firebase.firestore.FIRLoadBundleTaskState

@OptIn(ExperimentalForeignApi::class)
actual class FirebaseFirestore private constructor(internal val iosFirestore: FIRFirestore) {
    actual fun collection(collectionPath: String): CollectionReference {
        return CollectionReference(iosFirestore.collectionWithPath(collectionPath))
    }

    actual fun document(documentPath: String): DocumentReference {
        return DocumentReference(iosFirestore.documentWithPath(documentPath))
    }

    actual fun batch(): WriteBatch = WriteBatch(iosFirestore.batch())

    actual suspend fun <T> runTransaction(block: (Transaction) -> T): T = suspendCancellableCoroutine { cont ->
        iosFirestore.runTransactionWithBlock({ iosTransaction, errorPtr ->
            try {
                block(Transaction(iosTransaction!!))
            } catch (e: Exception) {
                cont.resumeWithException(e)
                null
            }
        }, completion = { result, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (result != null) {
                @Suppress("UNCHECKED_CAST")
                cont.resume(result as T)
            } else {
                cont.resumeWithException(RuntimeException("Transaction returned null"))
            }
        })
    }

    actual fun setSettings(settings: FirebaseFirestoreSettings) {
        iosFirestore.settings = settings.iosSettings
    }

    actual suspend fun clearPersistence() = suspendCancellableCoroutine<Unit> { cont ->
        iosFirestore.clearPersistenceWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    @OptIn(BetaInteropApi::class)
    actual suspend fun loadBundle(bundleData: ByteArray): LoadBundleTaskProgress = suspendCancellableCoroutine { cont ->
        val nsData = bundleData.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = bundleData.size.toULong())
        }
        iosFirestore.loadBundle(nsData) { progress, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (progress != null) {
                val state = when (progress.state) {
                    FIRLoadBundleTaskState.FIRLoadBundleTaskStateError -> LoadBundleTaskState.ERROR
                    FIRLoadBundleTaskState.FIRLoadBundleTaskStateInProgress -> LoadBundleTaskState.RUNNING
                    FIRLoadBundleTaskState.FIRLoadBundleTaskStateSuccess -> LoadBundleTaskState.SUCCESS
                    else -> LoadBundleTaskState.SUCCESS
                }
                cont.resume(
                    LoadBundleTaskProgress(
                        documentsLoaded = progress.documentsLoaded.toInt(),
                        totalDocuments = progress.totalDocuments.toInt(),
                        bytesLoaded = progress.bytesLoaded,
                        totalBytes = progress.totalBytes,
                        taskState = state
                    )
                )
            } else {
                cont.resumeWithException(RuntimeException("LoadBundleTaskProgress was null"))
            }
        }
    }

    actual suspend fun namedQuery(name: String): Query? = suspendCancellableCoroutine { cont ->
        iosFirestore.getQueryNamed(name) { query ->
            cont.resume(query?.let { Query(it) })
        }
    }

    actual companion object {
        actual fun getInstance(): FirebaseFirestore {
            return FirebaseFirestore(FIRFirestore.firestore())
        }
    }
}
