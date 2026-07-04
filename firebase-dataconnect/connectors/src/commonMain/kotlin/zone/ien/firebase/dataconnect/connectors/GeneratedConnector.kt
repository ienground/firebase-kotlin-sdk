package zone.ien.firebase.dataconnect.connectors

import zone.ien.firebase.dataconnect.FirebaseDataConnect

public expect class GeneratedConnector : FirebaseDataConnectConnector {
    public override val dataConnect: FirebaseDataConnect

    public companion object {
        public val instance: GeneratedConnector
    }
}
