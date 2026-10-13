package zone.ien.firebase.components

expect interface ComponentRegistrar {
    fun getComponents(): List<Component<*>>
}
