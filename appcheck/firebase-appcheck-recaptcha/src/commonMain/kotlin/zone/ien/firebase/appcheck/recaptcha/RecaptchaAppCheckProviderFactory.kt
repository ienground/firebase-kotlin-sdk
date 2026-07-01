package zone.ien.firebase.appcheck.recaptcha

import zone.ien.firebase.appcheck.AppCheckProviderFactory

public expect class RecaptchaAppCheckProviderFactory : AppCheckProviderFactory {
    public companion object {
        public fun getInstance(): RecaptchaAppCheckProviderFactory
    }
}
