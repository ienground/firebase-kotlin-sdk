package zone.ien.firebase.appcheck.recaptcha

import zone.ien.firebase.appcheck.AppCheckProviderFactory
import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.FIRAppCheckProviderFactoryProtocol

@OptIn(ExperimentalForeignApi::class)
public actual class RecaptchaAppCheckProviderFactory : AppCheckProviderFactory {

    override val iosFactory: FIRAppCheckProviderFactoryProtocol
        get() = throw UnsupportedOperationException("RecaptchaAppCheckProviderFactory is not supported on iOS. Apple platforms use AppAttest/DeviceCheck instead.")

    public actual companion object {
        public actual fun getInstance(): RecaptchaAppCheckProviderFactory {
            throw UnsupportedOperationException("RecaptchaAppCheckProviderFactory is not supported on iOS. Apple platforms use AppAttest/DeviceCheck instead.")
        }
    }
}
