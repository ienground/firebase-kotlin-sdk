package zone.ien.firebase.appcheck.debug

import zone.ien.firebase.appcheck.AppCheckProviderFactory
import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.FIRAppCheckDebugProviderFactory
import swiftPMImport.zone.ien.firebase.appcheck.firebase.appcheck.FIRAppCheckProviderFactoryProtocol

@OptIn(ExperimentalForeignApi::class)
public actual class DebugAppCheckProviderFactory : AppCheckProviderFactory {
    private val iosDebugFactory = FIRAppCheckDebugProviderFactory()
    override val iosFactory: FIRAppCheckProviderFactoryProtocol
        get() = iosDebugFactory
}
