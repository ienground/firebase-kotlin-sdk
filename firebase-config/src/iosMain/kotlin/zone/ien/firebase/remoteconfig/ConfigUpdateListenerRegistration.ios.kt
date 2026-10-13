package zone.ien.firebase.remoteconfig

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.config.FIRConfigUpdateListenerRegistration

public actual class ConfigUpdateListenerRegistration @OptIn(ExperimentalForeignApi::class) constructor(
    internal val iosRegistration: FIRConfigUpdateListenerRegistration
) {
    @OptIn(ExperimentalForeignApi::class)
    public actual fun remove() {
        iosRegistration.remove()
    }
}
