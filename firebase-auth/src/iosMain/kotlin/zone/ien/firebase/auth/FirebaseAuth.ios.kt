package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuth
import zone.ien.firebase.FirebaseApp
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
public actual class FirebaseAuth private actual constructor() {
    private lateinit var iosAuth: FIRAuth

    internal constructor(iosAuth: FIRAuth) : this() {
        this.iosAuth = iosAuth
    }

    public actual val currentUser: FirebaseUser?
        get() = iosAuth.currentUser()?.let { FirebaseUser(it) }

    public actual val authStateFlow: Flow<FirebaseUser?>
        get() = callbackFlow {
            val handle = iosAuth.addAuthStateDidChangeListener { _, user ->
                trySend(user?.let { FirebaseUser(it) })
            }
            awaitClose {
                iosAuth.removeAuthStateDidChangeListener(handle)
            }
        }

    public actual suspend fun signInAnonymously(): AuthResult = suspendCoroutine { cont ->
        iosAuth.signInAnonymouslyWithCompletion { result, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else if (result != null) {
                cont.resume(AuthResult(result))
            } else {
                cont.resumeWithException(Exception("Anonymous sign in returned null result and error"))
            }
        }
    }

    public actual suspend fun signInWithCustomToken(token: String): AuthResult = suspendCoroutine { cont ->
        iosAuth.signInWithCustomToken(token) { result, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else if (result != null) {
                cont.resume(AuthResult(result))
            } else {
                cont.resumeWithException(Exception("Custom token sign in returned null result and error"))
            }
        }
    }

    public actual suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult = suspendCoroutine { cont ->
        iosAuth.signInWithEmail(email = email, password = password) { result, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else if (result != null) {
                cont.resume(AuthResult(result))
            } else {
                cont.resumeWithException(Exception("Email sign in returned null result and error"))
            }
        }
    }

    public actual suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult = suspendCoroutine { cont ->
        iosAuth.createUserWithEmail(email, password) { result, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else if (result != null) {
                cont.resume(AuthResult(result))
            } else {
                cont.resumeWithException(Exception("Create user returned null result and error"))
            }
        }
    }

    public actual suspend fun signInWithCredential(credential: AuthCredential): AuthResult = suspendCoroutine { cont ->
        iosAuth.signInWithCredential(credential.iosCredential) { result, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else if (result != null) {
                cont.resume(AuthResult(result))
            } else {
                cont.resumeWithException(Exception("Sign in with credential returned null result and error"))
            }
        }
    }

    public actual fun signOut() {
        iosAuth.signOut(null)
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseAuth {
            return FirebaseAuth(FIRAuth.auth())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseAuth {
            val iosAuth = FIRAuth.authWithApp(app.iosApp)
                ?: throw IllegalStateException("FirebaseAuth instance could not be initialized for the given FirebaseApp.")
            return FirebaseAuth(iosAuth)
        }
    }
}
