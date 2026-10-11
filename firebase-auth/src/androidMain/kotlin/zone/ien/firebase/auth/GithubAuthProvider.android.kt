package zone.ien.firebase.auth

import com.google.firebase.auth.GithubAuthProvider as AndroidGithubAuthProvider

public actual object GithubAuthProvider {
    public actual fun getCredential(token: String): AuthCredential {
        return AuthCredential(AndroidGithubAuthProvider.getCredential(token))
    }
}
