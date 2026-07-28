package zone.ien.firebase.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser as AndroidFirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest as AndroidUserProfileChangeRequest
import kotlinx.coroutines.tasks.await

public actual class FirebaseUser internal constructor(
    internal val androidUser: AndroidFirebaseUser
) {
    public actual val uid: String
        get() = androidUser.uid

    public actual val email: String?
        get() = androidUser.email

    public actual val displayName: String?
        get() = androidUser.displayName

    public actual val photoUrl: String?
        get() = androidUser.photoUrl?.toString()

    public actual val phoneNumber: String?
        get() = androidUser.phoneNumber

    public actual val isEmailVerified: Boolean
        get() = androidUser.isEmailVerified

    public actual val isAnonymous: Boolean
        get() = androidUser.isAnonymous

    public actual suspend fun delete() {
        androidUser.delete().await()
    }

    public actual suspend fun getIdToken(forceRefresh: Boolean): String {
        val result = androidUser.getIdToken(forceRefresh).await()
        return result.token ?: throw FirebaseAuthException("Token is null", null)
    }

    public actual suspend fun unlink(provider: String): FirebaseUser {
        val result = androidUser.unlink(provider).await()
        val user = result.user ?: throw FirebaseAuthException("User is null after unlink", null)
        return FirebaseUser(user)
    }

    public actual suspend fun sendEmailVerification() {
        androidUser.sendEmailVerification().await()
    }

    public actual suspend fun updateProfile(request: UserProfileChangeRequest) {
        val androidReq = request.androidRequest ?: AndroidUserProfileChangeRequest.Builder()
            .setDisplayName(request.displayName)
            .setPhotoUri(request.photoUrl?.let { Uri.parse(it) })
            .build()
        androidUser.updateProfile(androidReq).await()
    }

    public actual suspend fun link(credential: AuthCredential): AuthResult {
        val result = androidUser.linkWithCredential(credential.androidCredential).await()
        return AuthResult(result)
    }

    public actual suspend fun updateEmail(email: String) {
        androidUser.updateEmail(email).await()
    }

    public actual suspend fun updatePassword(password: String) {
        androidUser.updatePassword(password).await()
    }

    public actual suspend fun reauthenticate(credential: AuthCredential) {
        androidUser.reauthenticate(credential.androidCredential).await()
    }

    public actual suspend fun reauthenticateAndRetrieveData(credential: AuthCredential): AuthResult {
        val result = androidUser.reauthenticateAndRetrieveData(credential.androidCredential).await()
        return AuthResult(result)
    }

    internal companion object {
        fun create(androidUser: AndroidFirebaseUser?): FirebaseUser? =
            androidUser?.let { FirebaseUser(it) }
    }
}
