package zone.ien.firebase.appcheck.recaptcha

import zone.ien.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.AppCheckProviderFactory as AndroidAppCheckProviderFactory
import com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory as AndroidRecaptchaAppCheckProviderFactory

public actual class RecaptchaAppCheckProviderFactory private constructor() : AppCheckProviderFactory {
    private lateinit var androidRecaptchaFactory: AndroidRecaptchaAppCheckProviderFactory

    internal constructor(androidRecaptchaFactory: AndroidRecaptchaAppCheckProviderFactory) : this() {
        this.androidRecaptchaFactory = androidRecaptchaFactory
    }

    override val androidFactory: AndroidAppCheckProviderFactory
        get() = androidRecaptchaFactory

    public actual companion object {
        public actual fun getInstance(): RecaptchaAppCheckProviderFactory {
            return RecaptchaAppCheckProviderFactory(AndroidRecaptchaAppCheckProviderFactory.getInstance())
        }
    }
}
