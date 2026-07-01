package zone.ien.firebase.database

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import swiftPMImport.zone.ien.firebase.firebase.database.FIRDatabaseReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

    public actual suspend fun setValue(value: Any?): Unit = suspendCancellableCoroutine { cont ->
        val iosValue = if (value is Map<*, *>) value as Map<Any?, *> else value
        iosReference.setValue(iosValue) { error, _ ->
            if (cont.isActive) {
                if (error != null) {
                    cont.resumeWithException(DatabaseException(error.localizedDescription, null))
                } else {
                    cont.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun removeValue(): Unit = suspendCancellableCoroutine { cont ->
        iosReference.removeValueWithCompletionBlock { error, _ ->
            if (cont.isActive) {
                if (error != null) {
                    cont.resumeWithException(DatabaseException(error.localizedDescription, null))
                } else {
                    cont.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun updateChildren(update: Map<String, Any?>): Unit = suspendCancellableCoroutine { cont ->
        iosReference.updateChildValues(update as Map<Any?, *>) { error, _ ->
            if (cont.isActive) {
                if (error != null) {
                    cont.resumeWithException(DatabaseException(error.localizedDescription, null))
                } else {
                    cont.resume(Unit)
                }
            }
        }
    }
}