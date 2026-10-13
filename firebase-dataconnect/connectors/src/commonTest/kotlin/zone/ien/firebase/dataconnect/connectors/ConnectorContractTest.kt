package zone.ien.firebase.dataconnect.connectors

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import zone.ien.firebase.dataconnect.ConnectorConfig
import zone.ien.firebase.dataconnect.DataConnectRequestStatus
import zone.ien.firebase.dataconnect.DataConnectQueryDescriptor
import zone.ien.firebase.dataconnect.DataConnectQueryReference
import zone.ien.firebase.dataconnect.DataConnectQueryResult
import zone.ien.firebase.dataconnect.DataConnectRequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.test.assertSame

class ConnectorContractTest {
    @Test
    fun commonMain_descriptor를_factory에서_실제_query로_사용한다() {
        val descriptor = object : DataConnectQueryDescriptor<String, Unit> {
            override val operationName: String = "GetMovie"
            override fun ref(variables: Unit): DataConnectQueryReference<String, Unit> =
                object : DataConnectQueryReference<String, Unit> {
                    override val operationName: String = "GetMovie"
                    override val variables: Unit = Unit
                    override val state: StateFlow<DataConnectRequestState<Unit>> = MutableStateFlow(
                        DataConnectRequestState(
                            executionId = 0,
                            operationName = operationName,
                            variables = Unit,
                            fetchPolicy = null,
                            status = DataConnectRequestStatus.PENDING
                        )
                    )

                    override suspend fun execute(fetchPolicy: zone.ien.firebase.dataconnect.DataConnectFetchPolicy): DataConnectQueryResult<String, Unit> =
                        DataConnectQueryResult(operationName, Unit, "Arrival", zone.ien.firebase.dataconnect.DataConnectDataSource.MEMORY)

                    override fun subscribe(): Flow<Result<DataConnectQueryResult<String, Unit>>> =
                        kotlinx.coroutines.flow.emptyFlow()
                }
        }

        val query = DataConnectOperationFactory().query(descriptor)

        assertSame(descriptor, query)
    }

    @Test
    fun testGeneratedConnectorDistinguishesConfigValues() {
        val production = ConnectorConfig("orders", "asia-northeast3", "mobile")
        val emulator = production.copy(location = "local")

        assertEquals("orders", production.service)
        assertEquals("asia-northeast3", production.location)
        assertEquals("local", emulator.location)
    }

    @Test
    fun testAtomicallyIssuesUniqueIdForConcurrentExecutions() = runBlocking {
        repeat(10) {
            val tracker = DataConnectRequestStateTracker("GetMovie", Unit)
            val ready = Channel<Unit>(capacity = 100)
            val release = CompletableDeferred<Unit>()
            val executions = List(100) {
                async(Dispatchers.Default) {
                    ready.send(Unit)
                    release.await()
                    tracker.begin(null)
                }
            }
            repeat(100) { ready.receive() }
            release.complete(Unit)

            val executionIds = executions.awaitAll()

            assertEquals(100, executionIds.toSet().size)
            assertEquals(100L, tracker.state.value.executionId)
        }
    }

    @Test
    fun testLateCompletionOfPreviousExecutionDoesNotOverwriteLatestState() {
        val tracker = DataConnectRequestStateTracker("GetMovie", Unit)
        val first = tracker.begin(null)
        val second = tracker.begin(null)

        tracker.complete(first, DataConnectRequestStatus.SUCCEEDED)

        assertEquals(second, tracker.state.value.executionId)
        assertEquals(DataConnectRequestStatus.PENDING, tracker.state.value.status)
    }

    @Test
    fun testSubscriptionEventSequenceIsIndependentOfExecuteState() {
        val tracker = DataConnectRequestStateTracker("GetMovie", Unit)
        val events = DataConnectSubscriptionEventSequence()

        repeat(3) { tracker.begin(null) }

        assertEquals(listOf(1L, 2L), listOf(events.next(), events.next()))
        assertEquals(3L, tracker.state.value.executionId)
    }

}
