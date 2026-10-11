package zone.ien.firebase.example

import zone.ien.firebase.appcheck.FirebaseAppCheck
import zone.ien.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory

public object AppCheckRecaptchaTest {
    public fun verifyRecaptchaContract() {
        try {
            // 1. Verify compilation & runtime access to RecaptchaAppCheckProviderFactory
            val factory = RecaptchaAppCheckProviderFactory.getInstance()
            println("RecaptchaAppCheckProviderFactory instance obtained successfully.")

            // 2. Verify installation flow
            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(factory)
            println("RecaptchaAppCheckProviderFactory registered in Firebase App Check.")
        } catch (e: UnsupportedOperationException) {
            // Expected fallback outcome on iOS
            println("RecaptchaAppCheckProviderFactory unsupported fallback: ${e.message}")
        } catch (e: Exception) {
            println("RecaptchaAppCheckProviderFactory installation error: ${e.message}")
        }
    }
}
