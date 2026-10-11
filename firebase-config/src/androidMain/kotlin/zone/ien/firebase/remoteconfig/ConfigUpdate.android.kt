package zone.ien.firebase.remoteconfig

import com.google.firebase.remoteconfig.ConfigUpdate as AndroidConfigUpdate

public actual class ConfigUpdate(
    internal val androidUpdate: AndroidConfigUpdate
) {
    public actual val updatedKeys: Set<String>
        get() = androidUpdate.updatedKeys
}
