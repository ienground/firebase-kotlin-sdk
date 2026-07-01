package zone.ien.firebase.database

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.database.FIRDatabaseReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
public actual class DatabaseReference(private val iosReference: FIRDatabaseReference) {

    public actual val key: String?
        get() = iosReference.key()

    public actual fun child(pathString: String): DatabaseReference {
        return DatabaseReference(iosReference.child(pathString))
    }

    public actual fun push(): DatabaseReference {
        return DatabaseReference(iosReference.childByAutoId())
    }

    public actual suspend fun setValue(value: Any?): Unit = suspendCoroutine { cont ->
        iosReference.setValue(value) { error, _ ->
            if (error != null) {
                cont.resumeWithException(DatabaseException(error.localizedDescription, null))
            } else {
                cont.resume(Unit)
            }
        }
    }

    public actual suspend fun removeValue(): Unit = suspendCoroutine { cont ->
        iosReference.removeValueWithCompletionBlock { error, _ ->
            if (error != null) {
                cont.resumeWithException(DatabaseException(error.localizedDescription, null))
            } else {
                cont.resume(Unit)
            }
        }
    }

    public actual suspend fun updateChildren(update: Map<String, Any?>): Unit = suspendCoroutine { cont ->
        val iosMap = update.mapKeys { it.key as Any? }
        iosReference.updateChildValues(iosMap) { error, _ ->
            if (error != null) {
                cont.resumeWithException(DatabaseException(error.localizedDescription, null))
            } else {
                cont.resume(Unit)
            }
        }
    }
}
