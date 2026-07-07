package zone.ien.firebase.remoteconfig

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.config.FIRRemoteConfigUpdate

public actual class ConfigUpdate @OptIn(ExperimentalForeignApi::class) constructor(
    internal val iosUpdate: FIRRemoteConfigUpdate
) {
    @OptIn(ExperimentalForeignApi::class)
    public actual val updatedKeys: Set<String>
        get() = iosUpdate.updatedKeys.map { it.toString() }.toSet()
}
