package zone.ien.firebase.dynamicloading

import zone.ien.firebase.components.Component
import zone.ien.firebase.components.ComponentRegistrar

expect class DynamicLoadingRegistrar : ComponentRegistrar {
    override fun getComponents(): List<Component<*>>
}
