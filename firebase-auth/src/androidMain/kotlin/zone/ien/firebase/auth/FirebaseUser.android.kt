package zone.ien.firebase.auth

import com.google.firebase.auth.FirebaseUser as AndroidFirebaseUser
import kotlinx.coroutines.tasks.await

public actual class FirebaseUser private actual constructor() {
    private var androidUser: AndroidFirebaseUser? = null

    public constructor(androidUser: AndroidFirebaseUser) : this() {
        this.androidUser = androidUser
    }

    public actual val uid: String
        get() = androidUser?.uid ?: ""

    public actual val email: String?
        get() = androidUser?.email

    public actual val isAnonymous: Boolean
        get() = androidUser?.isAnonymous ?: true

    public actual suspend fun delete() {
        androidUser?.delete()?.await()
    }

    public actual suspend fun getIdToken(forceRefresh: Boolean): String {
        return androidUser?.getIdToken(forceRefresh)?.await()?.token ?: ""
    }
}
