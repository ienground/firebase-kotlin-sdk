package zone.ien.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthModelsTest {
    @Test
    fun 프로필_변경_요청의_기본값은_비어_있다() {
        val request = UserProfileChangeRequest.Builder().build()

        assertNull(request.displayName)
        assertNull(request.photoUrl)
    }

    @Test
    fun 프로필_변경_요청에_이름과_사진을_설정한다() {
        val request = UserProfileChangeRequest.Builder()
            .setDisplayName("사용자")
            .setPhotoUri("https://example.com/profile.png")
            .build()

        assertEquals("사용자", request.displayName)
        assertEquals("https://example.com/profile.png", request.photoUrl)
    }

    @Test
    fun 프로필_변경_요청은_명시적으로_값을_제거할_수_있다() {
        val builder = UserProfileChangeRequest.Builder()
            .setDisplayName("사용자")
            .setPhotoUri("https://example.com/profile.png")

        val request = builder
            .setDisplayName(null)
            .setPhotoUri(null)
            .build()

        assertNull(request.displayName)
        assertNull(request.photoUrl)
    }

    @Test
    fun OAuth_공급자는_공급자_ID를_보존한다() {
        val provider = OAuthProvider("apple.com")

        assertEquals("apple.com", provider.providerId)
    }

    @Test
    fun Auth_호환성_확장함수_및_프로퍼티가_정상_참조된다() {
        val authStateChangedProp = FirebaseAuth::authStateChanged
        val credentialFn = EmailAuthProvider::credential
        val reauthFn = FirebaseUser::reauthenticateAndRetrieveData

        assertEquals("authStateChanged", authStateChangedProp.name)
        assertEquals("credential", credentialFn.name)
        assertEquals("reauthenticateAndRetrieveData", reauthFn.name)
    }
}
