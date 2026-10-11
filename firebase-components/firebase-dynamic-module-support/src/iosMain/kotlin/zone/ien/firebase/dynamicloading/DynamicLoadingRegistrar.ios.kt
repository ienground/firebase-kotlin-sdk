package zone.ien.firebase.dynamicloading

import zone.ien.firebase.components.Component
import zone.ien.firebase.components.ComponentRegistrar

actual class DynamicLoadingRegistrar : ComponentRegistrar {
    actual override fun getComponents(): List<Component<*>> = emptyList()
}
