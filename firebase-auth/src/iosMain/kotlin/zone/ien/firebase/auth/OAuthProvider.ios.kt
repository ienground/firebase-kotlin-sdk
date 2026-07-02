package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.auth.FIROAuthProvider

@OptIn(ExperimentalForeignApi::class)
public actual class OAuthProvider actual constructor(public actual val providerId: String) {

    public actual fun getCredential(idToken: String?, accessToken: String?): AuthCredential {
        val cred = FIROAuthProvider.credentialWithProviderID(
            providerID = providerId,
            IDToken = idToken ?: "",
            accessToken = accessToken ?: ""
        )
        return AuthCredential(cred)
    }

    public actual fun getCredential(idToken: String?, rawNonce: String?, accessToken: String?): AuthCredential {
        val cred = FIROAuthProvider.credentialWithProviderID(
            providerID = providerId,
            IDToken = idToken ?: "",
            rawNonce = rawNonce ?: "",
            accessToken = accessToken ?: ""
        )
        return AuthCredential(cred)
    }
}
