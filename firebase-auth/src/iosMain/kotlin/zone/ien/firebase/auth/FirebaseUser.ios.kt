package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseUser internal constructor(
    internal val iosUser: FIRUser
) {
    public actual val uid: String
        get() = iosUser.uid

    public actual val email: String?
        get() = iosUser.email

    public actual val displayName: String?
        get() = iosUser.displayName

    public actual val photoUrl: String?
        get() = iosUser.photoURL?.absoluteString

    public actual val phoneNumber: String?
        get() = iosUser.phoneNumber

    public actual val isEmailVerified: Boolean
        get() = iosUser.isEmailVerified()

    public actual val isAnonymous: Boolean
        get() = iosUser.isAnonymous()

    public actual suspend fun delete() {
        suspendCancellableCoroutine<Unit> { continuation ->
            iosUser.deleteWithCompletion { error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun getIdToken(forceRefresh: Boolean): String {
        return suspendCancellableCoroutine { continuation ->
            iosUser.getIDTokenForcingRefresh(forceRefresh) { token, error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else if (token != null) {
                    continuation.resume(token)
                } else {
                    continuation.resumeWithException(FirebaseAuthException("Token is null", null))
                }
            }
        }
    }

    public actual suspend fun unlink(provider: String): FirebaseUser {
        return suspendCancellableCoroutine { continuation ->
            iosUser.unlinkFromProvider(provider) { user, error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else if (user != null) {
                    continuation.resume(FirebaseUser(user))
                } else {
                    continuation.resumeWithException(FirebaseAuthException("User is null after unlink", null))
                }
            }
        }
    }

    public actual suspend fun sendEmailVerification() {
        suspendCancellableCoroutine<Unit> { continuation ->
            iosUser.sendEmailVerificationWithCompletion { error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun updateProfile(request: UserProfileChangeRequest) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val changeRequest = iosUser.profileChangeRequest()
            changeRequest.displayName = request.displayName
            changeRequest.photoURL = request.photoUrl?.let { platform.Foundation.NSURL.URLWithString(it) }
            changeRequest.commitChangesWithCompletion { error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun link(credential: AuthCredential): AuthResult {
        return suspendCancellableCoroutine { continuation ->
            iosUser.linkWithCredential(credential.iosCredential) { result, error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else if (result != null) {
                    continuation.resume(AuthResult(result))
                } else {
                    continuation.resumeWithException(FirebaseAuthException("AuthResult is null after link", null))
                }
            }
        }
    }

    public actual suspend fun updateEmail(email: String) {
        suspendCancellableCoroutine<Unit> { continuation ->
            iosUser.updateEmail(email) { error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun updatePassword(password: String) {
        suspendCancellableCoroutine<Unit> { continuation ->
            iosUser.updatePassword(password) { error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun reauthenticate(credential: AuthCredential) {
        suspendCancellableCoroutine<Unit> { continuation ->
            iosUser.reauthenticateWithCredential(credential.iosCredential) { _, error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }

    public actual suspend fun reauthenticateAndRetrieveData(credential: AuthCredential): AuthResult {
        return suspendCancellableCoroutine { continuation ->
            iosUser.reauthenticateWithCredential(credential.iosCredential) { result, error ->
                if (error != null) {
                    continuation.resumeWithException(FirebaseAuthException(error.localizedDescription, null))
                } else if (result != null) {
                    continuation.resume(AuthResult(result))
                } else {
                    continuation.resumeWithException(FirebaseAuthException("AuthResult is null after reauthenticate", null))
                }
            }
        }
    }

    internal companion object {
        fun create(iosUser: FIRUser?): FirebaseUser? =
            iosUser?.let { FirebaseUser(it) }
    }
}
