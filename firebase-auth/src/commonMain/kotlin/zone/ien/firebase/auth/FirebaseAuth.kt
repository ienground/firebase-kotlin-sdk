package zone.ien.firebase.auth

import kotlinx.coroutines.flow.Flow
import zone.ien.firebase.FirebaseApp

public expect class FirebaseAuth private constructor() {
    public val currentUser: FirebaseUser?
    public val authStateFlow: Flow<FirebaseUser?>

    public suspend fun signInAnonymously(): AuthResult
    public suspend fun signInWithCustomToken(token: String): AuthResult
    public suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult
    public suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult
    public suspend fun signInWithCredential(credential: AuthCredential): AuthResult
    public fun signOut()

    public companion object {
        public fun getInstance(): FirebaseAuth
        public fun getInstance(app: FirebaseApp): FirebaseAuth
    }
}
