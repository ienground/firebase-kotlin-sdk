package zone.ien.firebase.auth

import com.google.firebase.auth.FirebaseAuth as AndroidFirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import zone.ien.firebase.FirebaseApp

public actual class FirebaseAuth private actual constructor() {
    private lateinit var androidAuth: AndroidFirebaseAuth

    internal constructor(androidAuth: AndroidFirebaseAuth) : this() {
        this.androidAuth = androidAuth
    }

    public actual val currentUser: FirebaseUser?
        get() = androidAuth.currentUser?.let { FirebaseUser(it) }

    public actual val authStateFlow: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = AndroidFirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser?.let { FirebaseUser(it) })
            }
            androidAuth.addAuthStateListener(listener)
            awaitClose {
                androidAuth.removeAuthStateListener(listener)
            }
        }

    public actual suspend fun signInAnonymously(): AuthResult {
        return AuthResult(androidAuth.signInAnonymously().await())
    }

    public actual suspend fun signInWithCustomToken(token: String): AuthResult {
        return AuthResult(androidAuth.signInWithCustomToken(token).await())
    }

    public actual suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return AuthResult(androidAuth.signInWithEmailAndPassword(email, password).await())
    }

    public actual suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult {
        return AuthResult(androidAuth.createUserWithEmailAndPassword(email, password).await())
    }

    public actual suspend fun signInWithCredential(credential: AuthCredential): AuthResult {
        return AuthResult(androidAuth.signInWithCredential(credential.androidCredential).await())
    }

    public actual fun signOut() {
        androidAuth.signOut()
    }

    public actual fun useEmulator(host: String, port: Int) {
        androidAuth.useEmulator(host, port)
    }

    public actual suspend fun sendPasswordResetEmail(email: String) {
        androidAuth.sendPasswordResetEmail(email).await()
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseAuth {
            return FirebaseAuth(AndroidFirebaseAuth.getInstance())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseAuth {
            return FirebaseAuth(AndroidFirebaseAuth.getInstance(app.androidApp))
        }
    }
}
