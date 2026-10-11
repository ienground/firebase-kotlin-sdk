package zone.ien.firebase.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthModelsTest {
    @Test
    fun testUserProfileChangeRequestDefaultValuesEmpty() {
        val request = UserProfileChangeRequest.Builder().build()

        assertNull(request.displayName)
        assertNull(request.photoUrl)
    }

    @Test
    fun testUserProfileChangeRequestSetsNameAndPhoto() {
        val request = UserProfileChangeRequest.Builder()
            .setDisplayName("사용자")
            .setPhotoUri("https://example.com/profile.png")
            .build()

        assertEquals("사용자", request.displayName)
        assertEquals("https://example.com/profile.png", request.photoUrl)
    }

    @Test
    fun testUserProfileChangeRequestCanExplicitlyClearValues() {
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
    fun testAuthCompatibilityExtensionsAndProperties() {
        val authStateChangedProp = FirebaseAuth::authStateChanged
        val credentialFn = EmailAuthProvider::credential
        val reauthFn = FirebaseUser::reauthenticateAndRetrieveData

        assertEquals("authStateChanged", authStateChangedProp.name)
        assertEquals("credential", credentialFn.name)
        assertEquals("reauthenticateAndRetrieveData", reauthFn.name)
    }
}
