package zone.ien.firebase.auth

import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.FirebaseUser as AndroidFirebaseUser
import kotlinx.coroutines.tasks.await

public actual class FirebaseUser private actual constructor() {
    private lateinit var androidUser: AndroidFirebaseUser

    public constructor(androidUser: AndroidFirebaseUser) : this() {
        this.androidUser = androidUser
    }

    public actual val uid: String
        get() = androidUser.uid

    public actual val email: String?
        get() = androidUser.email

    public actual val isAnonymous: Boolean
        get() = androidUser.isAnonymous

    public actual suspend fun updateProfile(request: UserProfileChangeRequest) {
        androidUser.updateProfile(request.androidRequest).await()
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

    public actual suspend fun unlink(provider: String): FirebaseUser {
        val result = androidUser.unlink(provider).await()
        return FirebaseUser(result.user!!)
    }

    public actual suspend fun sendEmailVerification() {
        androidUser.sendEmailVerification().await()
    }

    public actual suspend fun delete() {
        androidUser.delete().await()
    }

    public actual suspend fun getIdToken(forceRefresh: Boolean): String {
        return androidUser.getIdToken(forceRefresh).await().token ?: ""
    }
}