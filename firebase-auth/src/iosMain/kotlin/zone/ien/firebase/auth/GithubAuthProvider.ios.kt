package zone.ien.firebase.auth

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRGitHubAuthProvider

@OptIn(ExperimentalForeignApi::class)
public actual object GithubAuthProvider {
    public actual fun getCredential(token: String): AuthCredential {
        val cred = FIRGitHubAuthProvider.credentialWithToken(token)
        return AuthCredential(cred)
    }
}
