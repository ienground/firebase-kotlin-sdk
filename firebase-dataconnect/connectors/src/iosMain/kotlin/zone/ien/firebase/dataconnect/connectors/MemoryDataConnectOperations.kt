package zone.ien.firebase.dataconnect.connectors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.channels.BufferOverflow
import zone.ien.firebase.dataconnect.DataConnectDataSource
import zone.ien.firebase.dataconnect.DataConnectFailureKind
import zone.ien.firebase.dataconnect.DataConnectFetchPolicy
import zone.ien.firebase.dataconnect.DataConnectMutation
import zone.ien.firebase.dataconnect.DataConnectMutationDescriptor
import zone.ien.firebase.dataconnect.DataConnectMutationReference
import zone.ien.firebase.dataconnect.DataConnectMutationResult
import zone.ien.firebase.dataconnect.DataConnectOperationException
import zone.ien.firebase.dataconnect.DataConnectQuery
import zone.ien.firebase.dataconnect.DataConnectQueryDescriptor
import zone.ien.firebase.dataconnect.DataConnectQueryReference
import zone.ien.firebase.dataconnect.DataConnectQueryResult
import zone.ien.firebase.dataconnect.DataConnectRequestState
import zone.ien.firebase.dataconnect.DataConnectRequestStatus

public fun <Data, Variables> DataConnectOperationFactory.seededQuery(
    operationName: String,
    seedData: Data
): DataConnectQuery<Data, Variables> = query(seededQueryDescriptor(operationName, seedData))

public fun <Data, Variables> DataConnectOperationFactory.seededQueryDescriptor(
    operationName: String,
    seedData: Data
): DataConnectQueryDescriptor<Data, Variables> = MemoryQuery(operationName, MemorySeed.Present(seedData))

public fun <Data, Variables> DataConnectOperationFactory.query(
    operationName: String
): DataConnectQuery<Data, Variables> = query(queryDescriptor(operationName))

public fun <Data, Variables> DataConnectOperationFactory.queryDescriptor(
    operationName: String
): DataConnectQueryDescriptor<Data, Variables> = MemoryQuery(operationName, MemorySeed.Absent)

public fun <Data, Variables> DataConnectOperationFactory.mutation(
    operationName: String
): DataConnectMutation<Data, Variables> = mutation(mutationDescriptor(operationName))

public fun <Data, Variables> DataConnectOperationFactory.mutationDescriptor(
    operationName: String
): DataConnectMutationDescriptor<Data, Variables> = MemoryMutation(operationName)

private sealed interface MemorySeed<out Data> {
    data class Present<Data>(val data: Data) : MemorySeed<Data>
    data object Absent : MemorySeed<Nothing>
}

private class MemoryQuery<Data, Variables>(
    override val operationName: String,
    private val seed: MemorySeed<Data>
) : DataConnectQueryDescriptor<Data, Variables> {
    override fun ref(variables: Variables): DataConnectQueryReference<Data, Variables> =
        MemoryQueryReference(operationName, variables, seed)
}

private class MemoryMutation<Data, Variables>(
    override val operationName: String
) : DataConnectMutationDescriptor<Data, Variables> {
    override fun ref(variables: Variables): DataConnectMutationReference<Data, Variables> =
        MemoryMutationReference(operationName, variables)
}

private class MemoryQueryReference<Data, Variables>(
    override val operationName: String,
    override val variables: Variables,
    private val seed: MemorySeed<Data>
) : DataConnectQueryReference<Data, Variables> {
    private val stateTracker = DataConnectRequestStateTracker(operationName, variables)
    private val updates = MutableSharedFlow<Result<DataConnectQueryResult<Data, Variables>>>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val state: StateFlow<DataConnectRequestState<Variables>> = stateTracker.state

    override suspend fun execute(fetchPolicy: DataConnectFetchPolicy): DataConnectQueryResult<Data, Variables> {
        val executionId = stateTracker.begin(fetchPolicy)
        if (fetchPolicy == DataConnectFetchPolicy.SERVER_ONLY) {
            throw fail(executionId, DataConnectRequestStatus.UNSUPPORTED, DataConnectFailureKind.BRIDGE_REQUIRED)
        }
        val data = when (seed) {
            is MemorySeed.Present -> seed.data
            MemorySeed.Absent -> throw fail(
                executionId,
                DataConnectRequestStatus.FAILED,
                DataConnectFailureKind.OPERATION
            )
        }
        return DataConnectQueryResult(
            operationName = operationName,
            variables = variables,
            data = data,
            dataSource = DataConnectDataSource.MEMORY,
            executionId = executionId
        ).also {
            stateTracker.complete(executionId, DataConnectRequestStatus.SUCCEEDED)
            updates.tryEmit(Result.success(it))
        }
    }

    override fun subscribe(): Flow<Result<DataConnectQueryResult<Data, Variables>>> = updates.asSharedFlow()

    private suspend fun fail(
        executionId: Long,
        status: DataConnectRequestStatus,
        kind: DataConnectFailureKind
    ): DataConnectOperationException {
        val exception = DataConnectOperationException(operationName, kind)
        stateTracker.complete(executionId, status, exception)
        updates.tryEmit(Result.failure(exception))
        return exception
    }

}

private class MemoryMutationReference<Data, Variables>(
    override val operationName: String,
    override val variables: Variables
) : DataConnectMutationReference<Data, Variables> {
    private val stateTracker = DataConnectRequestStateTracker(operationName, variables)
    override val state: StateFlow<DataConnectRequestState<Variables>> = stateTracker.state

    override suspend fun execute(): DataConnectMutationResult<Data, Variables> {
        val exception = DataConnectOperationException(operationName, DataConnectFailureKind.BRIDGE_REQUIRED)
        val executionId = stateTracker.begin(null)
        stateTracker.complete(executionId, DataConnectRequestStatus.UNSUPPORTED, exception)
        throw exception
    }
}
