package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRUser
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuthDataResult
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

    public actual suspend fun reauthenticate(credential: AuthCredential): Unit = suspendCancellableCoroutine { cont ->
        iosUser.reauthenticateWithCredential(credential.iosCredential) { _, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    public actual suspend fun unlink(provider: String): FirebaseUser = suspendCancellableCoroutine { cont ->
        iosUser.unlinkFromProvider(provider) { authDataResult, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else {
                val dataResult = authDataResult as? FIRAuthDataResult
                if (dataResult != null) {
                    cont.resume(FirebaseUser(dataResult.user))
                } else {
                    cont.resumeWithException(Exception("Unlink returned null user and error"))
                }
            }
        }
    }

    public actual suspend fun sendEmailVerification(): Unit = suspendCancellableCoroutine { cont ->
        iosUser.sendEmailVerificationWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }

    public actual suspend fun updateProfile(request: UserProfileChangeRequest): Unit = suspendCancellableCoroutine { cont ->
        val changeRequest = iosUser.profileChangeRequest()
        changeRequest.displayName = request.displayName
        changeRequest.photoURL = request.photoUrl?.let { platform.Foundation.NSURL.URLWithString(it) }
        changeRequest.commitChangesWithCompletion { error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else {
                cont.resume(Unit)
            }
        }
    }
}