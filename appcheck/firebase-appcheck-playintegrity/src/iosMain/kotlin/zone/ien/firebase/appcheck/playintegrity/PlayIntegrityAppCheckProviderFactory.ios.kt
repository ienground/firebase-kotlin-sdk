package zone.ien.firebase.appcheck.playintegrity

import zone.ien.firebase.appcheck.AppCheckProviderFactory
import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.FIRAppCheckProviderFactoryProtocol

@OptIn(ExperimentalForeignApi::class)
public actual class PlayIntegrityAppCheckProviderFactory : AppCheckProviderFactory {

    override val iosFactory: FIRAppCheckProviderFactoryProtocol
        get() = throw UnsupportedOperationException("PlayIntegrityAppCheckProviderFactory is only supported on Android.")

    public actual companion object {
        public actual fun getInstance(): PlayIntegrityAppCheckProviderFactory {
            throw UnsupportedOperationException("PlayIntegrityAppCheckProviderFactory is only supported on Android.")
        }
    }
}
