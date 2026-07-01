package zone.ien.firebase.datatransport

import zone.ien.firebase.components.ComponentRegistrar

public actual class TransportRegistrar actual constructor() : ComponentRegistrar {
    override fun getComponents(): List<zone.ien.firebase.components.Component<*>> = emptyList()
}
