package zone.ien.firebase.auth

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
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

    public actual suspend fun signInAnonymously(): AuthResult = suspendCancellableCoroutine { cont ->
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

    public actual suspend fun signInWithCustomToken(token: String): AuthResult = suspendCancellableCoroutine { cont ->
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

    public actual suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult = suspendCancellableCoroutine { cont ->
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

    public actual suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult = suspendCancellableCoroutine { cont ->
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

    public actual suspend fun signInWithCredential(credential: AuthCredential): AuthResult = suspendCancellableCoroutine { cont ->
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

    @OptIn(BetaInteropApi::class)
    public actual fun signOut() {
        memScoped {
            val errorPointer = alloc<ObjCObjectVar<platform.Foundation.NSError?>>()
            val success = iosAuth.signOut(errorPointer.ptr)
            if (!success) {
                val error = errorPointer.value
                throw Exception(error?.localizedDescription ?: "Sign out failed")
            }
        }
    }

    public actual companion object {
        public actual fun getInstance(): FirebaseAuth {
            return FirebaseAuth(FIRAuth.auth())
        }

        public actual fun getInstance(app: FirebaseApp): FirebaseAuth {
            val iosAuth = FIRAuth.authWithApp(app.iosApp)
            return FirebaseAuth(iosAuth)
        }
    }
}
