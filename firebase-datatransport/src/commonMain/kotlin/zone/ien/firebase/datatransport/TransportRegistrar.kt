package zone.ien.firebase.datatransport

import zone.ien.firebase.components.Component
import zone.ien.firebase.components.ComponentRegistrar

public expect class TransportRegistrar : ComponentRegistrar {
    public constructor()
    override fun getComponents(): List<Component<*>>
}
