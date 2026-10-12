package zone.ien.firebase.appcheck.recaptcha

import zone.ien.firebase.appcheck.AppCheckProviderFactory

public expect class RecaptchaAppCheckProviderFactory : AppCheckProviderFactory {
    private constructor()
    public companion object {
        /** 주어진 reCAPTCHA Enterprise 사이트 키를 사용하는 팩터리를 만듭니다. */
        public fun getInstance(siteKey: String): RecaptchaAppCheckProviderFactory
    }
}
