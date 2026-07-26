package zone.ien.firebase.dataconnect.connectors

import com.google.firebase.dataconnect.DataConnectException as AndroidDataConnectException
import com.google.firebase.dataconnect.DataConnectOperationException as AndroidOperationException
import com.google.firebase.dataconnect.DataConnectPathSegment as AndroidPathSegment
import com.google.firebase.dataconnect.DataSource as AndroidDataSource
import com.google.firebase.dataconnect.MutationRef as AndroidMutationReference
import com.google.firebase.dataconnect.MutationResult as AndroidMutationResult
import com.google.firebase.dataconnect.QueryRef as AndroidQueryReference
import com.google.firebase.dataconnect.QueryResult as AndroidQueryResult
import com.google.firebase.dataconnect.generated.GeneratedConnector as AndroidGeneratedConnector
import com.google.firebase.dataconnect.generated.GeneratedMutation as AndroidGeneratedMutation
import com.google.firebase.dataconnect.generated.GeneratedQuery as AndroidGeneratedQuery
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import zone.ien.firebase.dataconnect.DataConnectDataSource
import zone.ien.firebase.dataconnect.DataConnectError
import zone.ien.firebase.dataconnect.DataConnectErrorPathSegment
import zone.ien.firebase.dataconnect.DataConnectFailureKind
import zone.ien.firebase.dataconnect.DataConnectFailureResponse
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

public fun <Connector : AndroidGeneratedConnector<Connector>, Data, Variables> DataConnectOperationFactory.query(
    operation: AndroidGeneratedQuery<Connector, Data, Variables>
): DataConnectQuery<Data, Variables> = query(queryDescriptor(operation))

public fun <Connector : AndroidGeneratedConnector<Connector>, Data, Variables> DataConnectOperationFactory.mutation(
    operation: AndroidGeneratedMutation<Connector, Data, Variables>
): DataConnectMutation<Data, Variables> = mutation(mutationDescriptor(operation))

public fun <Connector : AndroidGeneratedConnector<Connector>, Data, Variables> DataConnectOperationFactory.queryDescriptor(
    operation: AndroidGeneratedQuery<Connector, Data, Variables>
): DataConnectQueryDescriptor<Data, Variables> = AndroidQueryAdapter(operation)

public fun <Connector : AndroidGeneratedConnector<Connector>, Data, Variables> DataConnectOperationFactory.mutationDescriptor(
    operation: AndroidGeneratedMutation<Connector, Data, Variables>
): DataConnectMutationDescriptor<Data, Variables> = AndroidMutationAdapter(operation)

private class AndroidQueryAdapter<Connector : AndroidGeneratedConnector<Connector>, Data, Variables>(
    private val operation: AndroidGeneratedQuery<Connector, Data, Variables>
) : DataConnectQueryDescriptor<Data, Variables> {
    override val operationName: String
        get() = operation.operationName

    override fun ref(variables: Variables): DataConnectQueryReference<Data, Variables> =
        AndroidQueryReferenceAdapter(operation.ref(variables))
}

private class AndroidMutationAdapter<Connector : AndroidGeneratedConnector<Connector>, Data, Variables>(
    private val operation: AndroidGeneratedMutation<Connector, Data, Variables>
) : DataConnectMutationDescriptor<Data, Variables> {
    override val operationName: String
        get() = operation.operationName

    override fun ref(variables: Variables): DataConnectMutationReference<Data, Variables> =
        AndroidMutationReferenceAdapter(operation.ref(variables))
}

private class AndroidQueryReferenceAdapter<Data, Variables>(
    private val reference: AndroidQueryReference<Data, Variables>
) : DataConnectQueryReference<Data, Variables> {
    private val stateTracker = DataConnectRequestStateTracker(reference.operationName, reference.variables)
    private val subscriptionEvents = DataConnectSubscriptionEventSequence()
    override val state: StateFlow<DataConnectRequestState<Variables>> = stateTracker.state
    override val operationName: String get() = reference.operationName
    override val variables: Variables get() = reference.variables

    override suspend fun execute(fetchPolicy: DataConnectFetchPolicy): DataConnectQueryResult<Data, Variables> {
        return executeAndroidQuery(stateTracker, fetchPolicy) { executionId ->
            reference.execute(fetchPolicy.toAndroid()).toCommon(executionId)
        }
    }

    override fun subscribe(): Flow<Result<DataConnectQueryResult<Data, Variables>>> =
        reference.subscribe().flow.map { update ->
            val executionId = subscriptionEvents.next()
            update.result.fold(
                onSuccess = { Result.success(it.toCommon(executionId)) },
                onFailure = { Result.failure(mapAndroidFailure(operationName, it)) }
            )
        }

}

private class AndroidMutationReferenceAdapter<Data, Variables>(
    private val reference: AndroidMutationReference<Data, Variables>
) : DataConnectMutationReference<Data, Variables> {
    private val stateTracker = DataConnectRequestStateTracker(reference.operationName, reference.variables)
    override val state: StateFlow<DataConnectRequestState<Variables>> = stateTracker.state
    override val operationName: String get() = reference.operationName
    override val variables: Variables get() = reference.variables

    override suspend fun execute(): DataConnectMutationResult<Data, Variables> {
        return executeAndroidMutation(stateTracker) { executionId ->
            reference.execute().toCommon(executionId)
        }
    }
}

internal suspend fun <Result, Variables> executeAndroidQuery(
    stateTracker: DataConnectRequestStateTracker<Variables>,
    fetchPolicy: DataConnectFetchPolicy,
    operation: suspend (Long) -> Result
): Result = executeAndroidOperation(stateTracker, fetchPolicy, operation)

internal suspend fun <Result, Variables> executeAndroidMutation(
    stateTracker: DataConnectRequestStateTracker<Variables>,
    operation: suspend (Long) -> Result
): Result = executeAndroidOperation(stateTracker, null, operation)

private suspend fun <Result, Variables> executeAndroidOperation(
    stateTracker: DataConnectRequestStateTracker<Variables>,
    fetchPolicy: DataConnectFetchPolicy?,
    operation: suspend (Long) -> Result
): Result {
    val executionId = stateTracker.begin(fetchPolicy)
    return try {
        operation(executionId).also {
            stateTracker.complete(executionId, DataConnectRequestStatus.SUCCEEDED)
        }
    } catch (cancellation: CancellationException) {
        stateTracker.complete(executionId, DataConnectRequestStatus.CANCELLED)
        throw cancellation
    } catch (throwable: Throwable) {
        val mapped = mapAndroidFailure(stateTracker.operationName, throwable)
        stateTracker.complete(
            executionId,
            DataConnectRequestStatus.FAILED,
            mapped as? DataConnectOperationException
        )
        throw mapped
    }
}

private fun DataConnectFetchPolicy.toAndroid(): AndroidQueryReference.FetchPolicy = when (this) {
    DataConnectFetchPolicy.PREFER_CACHE -> AndroidQueryReference.FetchPolicy.PREFER_CACHE
    DataConnectFetchPolicy.CACHE_ONLY -> AndroidQueryReference.FetchPolicy.CACHE_ONLY
    DataConnectFetchPolicy.SERVER_ONLY -> AndroidQueryReference.FetchPolicy.SERVER_ONLY
}

private fun AndroidDataSource.toCommon(): DataConnectDataSource = when (this) {
    AndroidDataSource.CACHE -> DataConnectDataSource.CACHE
    AndroidDataSource.SERVER -> DataConnectDataSource.SERVER
}

private fun <Data, Variables> AndroidQueryResult<Data, Variables>.toCommon(executionId: Long) =
    DataConnectQueryResult(ref.operationName, ref.variables, data, dataSource.toCommon(), executionId)

private fun <Data, Variables> AndroidMutationResult<Data, Variables>.toCommon(executionId: Long) =
    DataConnectMutationResult(ref.operationName, ref.variables, data, executionId)

internal fun mapAndroidFailure(operationName: String, throwable: Throwable): Throwable = when (throwable) {
    is CancellationException -> throwable
    is AndroidOperationException -> DataConnectOperationException(
        operationName = operationName,
        kind = DataConnectFailureKind.OPERATION,
        response = DataConnectFailureResponse(
            rawData = throwable.response.rawData,
            data = throwable.response.data,
            errors = throwable.response.errors.map { error ->
                DataConnectError(
                    message = error.message,
                    path = error.path.map { segment ->
                        when (segment) {
                            is AndroidPathSegment.Field -> DataConnectErrorPathSegment.Field(segment.field)
                            is AndroidPathSegment.ListIndex -> DataConnectErrorPathSegment.ListIndex(segment.index)
                        }
                    }
                )
            }
        ),
        cause = throwable
    )
    is AndroidDataConnectException -> DataConnectOperationException(
        operationName = operationName,
        kind = DataConnectFailureKind.TRANSPORT,
        cause = throwable
    )
    else -> throwable
}
