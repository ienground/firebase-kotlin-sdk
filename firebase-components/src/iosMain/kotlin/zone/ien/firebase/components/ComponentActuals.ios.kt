package zone.ien.firebase.components

actual class Component<T>

actual interface ComponentRegistrar {
    actual fun getComponents(): List<Component<*>>
}
