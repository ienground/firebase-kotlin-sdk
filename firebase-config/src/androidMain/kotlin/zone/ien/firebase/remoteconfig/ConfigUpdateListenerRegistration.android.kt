package zone.ien.firebase.remoteconfig

import com.google.firebase.remoteconfig.ConfigUpdateListenerRegistration as AndroidRegistration

public actual class ConfigUpdateListenerRegistration(
    internal val androidRegistration: AndroidRegistration
) {
    public actual fun remove() {
        androidRegistration.remove()
    }
}
