package zone.ien.firebase.dataconnect.connectors

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import zone.ien.firebase.dataconnect.DataConnectFetchPolicy
import zone.ien.firebase.dataconnect.DataConnectOperationException
import zone.ien.firebase.dataconnect.DataConnectRequestState
import zone.ien.firebase.dataconnect.DataConnectRequestStatus

internal class DataConnectRequestStateTracker<Variables>(
    internal val operationName: String,
    variables: Variables
) {
    private val mutableState = MutableStateFlow(
        DataConnectRequestState(0, operationName, variables, null, DataConnectRequestStatus.PENDING)
    )
    val state: StateFlow<DataConnectRequestState<Variables>> = mutableState.asStateFlow()

    fun begin(fetchPolicy: DataConnectFetchPolicy?): Long {
        var executionId = 0L
        mutableState.update {
            executionId = it.executionId + 1
            it.copy(
                executionId = executionId,
                fetchPolicy = fetchPolicy,
                status = DataConnectRequestStatus.PENDING,
                failure = null
            )
        }
        return executionId
    }

    fun complete(
        executionId: Long,
        status: DataConnectRequestStatus,
        failure: DataConnectOperationException? = null
    ) {
        mutableState.update {
            if (it.executionId == executionId) it.copy(status = status, failure = failure) else it
        }
    }
}

@OptIn(ExperimentalAtomicApi::class)
internal class DataConnectSubscriptionEventSequence {
    private val sequence = AtomicLong(0L)

    fun next(): Long = sequence.addAndFetch(1L)
}
