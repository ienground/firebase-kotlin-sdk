package zone.ien.firebase.dynamicloading

import zone.ien.firebase.components.Component
import zone.ien.firebase.components.ComponentRegistrar

actual class DynamicLoadingRegistrar : ComponentRegistrar {
    actual override fun getComponents(): List<Component<*>> {
        // Dynamic modules support is specific to Play Feature Delivery on Android.
        // It is unsupported and returns empty components on iOS.
        return emptyList()
    }
}
