package zone.ien.firebase.database

import com.google.firebase.database.DatabaseReference as AndroidDatabaseReference
import com.google.firebase.database.Transaction as AndroidTransaction
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

public actual class DatabaseReference(private val androidReference: AndroidDatabaseReference) {

    public actual val key: String?
        get() = androidReference.key

    public actual fun child(pathString: String): DatabaseReference {
        return DatabaseReference(androidReference.child(pathString))
    }

    public actual fun push(): DatabaseReference {
        return DatabaseReference(androidReference.push())
    }

    public actual suspend fun setValue(value: Any?) {
        androidReference.setValue(value).await()
    }

    public actual suspend fun removeValue() {
        androidReference.removeValue().await()
    }

    public actual suspend fun updateChildren(update: Map<String, Any?>) {
        androidReference.updateChildren(update).await()
     }

    public actual suspend fun runTransaction(handler: (MutableData) -> TransactionResult): TransactionResult = suspendCancellableCoroutine { cont ->
        androidReference.runTransaction(object : AndroidTransaction.Handler {
            override fun doTransaction(currentData: com.google.firebase.database.MutableData): AndroidTransaction.Result {
                val result = handler(MutableData(currentData))
                return when (result) {
                    TransactionResult.SUCCESS -> AndroidTransaction.success(currentData)
                    TransactionResult.ABORT -> AndroidTransaction.abort()
                }
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: com.google.firebase.database.DataSnapshot?) {
                if (error != null) {
                    cont.resumeWithException(error.toException())
                } else {
                    cont.resume(if (committed) TransactionResult.SUCCESS else TransactionResult.ABORT)
                }
            }
        })
    }

    public actual fun keepSynced(keepSynced: Boolean) {
        androidReference.keepSynced(keepSynced)
    }
}
