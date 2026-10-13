package zone.ien.firebase.dataconnect.connectors

import zone.ien.firebase.dataconnect.FirebaseDataConnect

@Deprecated("영화 예제용 호환 shim입니다. DefaultFirebaseDataConnectConnector를 사용하세요.")
public expect class GeneratedConnector private constructor() :
    FirebaseDataConnectConnector,
    FirebaseDataConnectOperationsProvider {
    public override val dataConnect: FirebaseDataConnect
    public override val operations: DataConnectOperationFactory

    public companion object {
        public val instance: GeneratedConnector
    }
}
