package zone.ien.firebase.dataconnect.connectors

import zone.ien.firebase.dataconnect.ConnectorConfig
import zone.ien.firebase.dataconnect.DataConnectMutation
import zone.ien.firebase.dataconnect.DataConnectMutationDescriptor
import zone.ien.firebase.dataconnect.DataConnectQuery
import zone.ien.firebase.dataconnect.DataConnectQueryDescriptor
import zone.ien.firebase.dataconnect.FirebaseDataConnect

public class DataConnectOperationFactory {
    public fun <Data, Variables> query(
        descriptor: DataConnectQueryDescriptor<Data, Variables>
    ): DataConnectQuery<Data, Variables> = descriptor

    public fun <Data, Variables> mutation(
        descriptor: DataConnectMutationDescriptor<Data, Variables>
    ): DataConnectMutation<Data, Variables> = descriptor
}

public interface FirebaseDataConnectConnector {
    public val dataConnect: FirebaseDataConnect
}

public interface FirebaseDataConnectOperationsProvider {
    public val operations: DataConnectOperationFactory
}

public val FirebaseDataConnectConnector.operations: DataConnectOperationFactory
    get() = (this as? FirebaseDataConnectOperationsProvider)?.operations
        ?: DataConnectOperationFactory()

public class DefaultFirebaseDataConnectConnector private constructor(
    override val dataConnect: FirebaseDataConnect,
    override val operations: DataConnectOperationFactory
) : FirebaseDataConnectConnector, FirebaseDataConnectOperationsProvider {
    public fun copy(
        dataConnect: FirebaseDataConnect = this.dataConnect,
        operations: DataConnectOperationFactory = this.operations
    ): DefaultFirebaseDataConnectConnector = DefaultFirebaseDataConnectConnector(dataConnect, operations)

    public companion object {
        public fun getInstance(config: ConnectorConfig): DefaultFirebaseDataConnectConnector =
            DefaultFirebaseDataConnectConnector(
                dataConnect = FirebaseDataConnect.getInstance(config),
                operations = DataConnectOperationFactory()
            )
    }
}
