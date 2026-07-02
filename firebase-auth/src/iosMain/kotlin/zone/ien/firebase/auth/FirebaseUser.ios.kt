package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseUser private actual constructor() {
    private lateinit var iosUser: FIRUser

    public constructor(iosUser: FIRUser) : this() {
        this.iosUser = iosUser
    }

    public actual val uid: String
        get() = iosUser.uid

    public actual val email: String?
        get() = iosUser.email

    public actual val isAnonymous: Boolean
        get() = iosUser.isAnonymous()

    public actual suspend fun delete(): Unit = suspendCancellableCoroutine { cont ->
        iosUser.deleteWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    public actual suspend fun getIdToken(forceRefresh: Boolean): String = suspendCancellableCoroutine { cont ->
            iosUser.getIDTokenForcingRefresh(forceRefresh) { token, error ->
                if (error != null) {
                    cont.resumeWithException(Exception(error.localizedDescription))
                } else {
                    cont.resume(token ?: "")
                }
            }
        }
}