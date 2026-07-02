package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRGoogleAuthProvider

@OptIn(ExperimentalForeignApi::class)
public actual object GoogleAuthProvider {
    public actual fun getCredential(idToken: String?, accessToken: String?): AuthCredential {
        val cred = FIRGoogleAuthProvider.credentialWithIDToken(idToken ?: "", accessToken ?: "")
        return AuthCredential(cred)
    }
}
