package zone.ien.firebase.appcheck

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.FIRAppCheckProviderFactoryProtocol

@OptIn(ExperimentalForeignApi::class)
public actual interface AppCheckProviderFactory {
    public val iosFactory: FIRAppCheckProviderFactoryProtocol
}
