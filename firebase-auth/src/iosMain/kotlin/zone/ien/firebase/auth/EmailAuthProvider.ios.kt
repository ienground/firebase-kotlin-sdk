package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.auth.FIREmailAuthProvider

@OptIn(ExperimentalForeignApi::class)
public actual object EmailAuthProvider {
    public actual fun getCredential(email: String, password: String): AuthCredential {
        return AuthCredential(FIREmailAuthProvider.credentialWithEmail(email, password = password))
    }
}
