package zone.ien.firebase.auth

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

    public actual suspend fun delete() {
        androidUser.delete().await()
    }

    public actual suspend fun getIdToken(forceRefresh: Boolean): String {
        return androidUser.getIdToken(forceRefresh).await().token ?: ""
    }

    public actual suspend fun reauthenticate(credential: AuthCredential) {
        androidUser.reauthenticate(credential.androidCredential).await()
    }

    public actual suspend fun unlink(provider: String): FirebaseUser {
        val result = androidUser.unlink(provider).await()
        val user = result.user ?: throw Exception("Unlink returned a null user.")
        return FirebaseUser(user)
    }

    public actual suspend fun sendEmailVerification() {
        androidUser.sendEmailVerification().await()
    }

    public actual suspend fun updateProfile(request: UserProfileChangeRequest) {
        androidUser.updateProfile(request.androidRequest).await()
    }
}