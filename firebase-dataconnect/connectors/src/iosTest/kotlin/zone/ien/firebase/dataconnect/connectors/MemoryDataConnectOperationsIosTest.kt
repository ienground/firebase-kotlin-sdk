package zone.ien.firebase.dataconnect.connectors

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import zone.ien.firebase.dataconnect.ConnectorConfig
import zone.ien.firebase.dataconnect.DataConnectDataSource
import zone.ien.firebase.dataconnect.DataConnectFailureKind
import zone.ien.firebase.dataconnect.DataConnectFetchPolicy
import zone.ien.firebase.dataconnect.DataConnectOperationException
import zone.ien.firebase.dataconnect.DataConnectRequestStatus

class MemoryDataConnectOperationsIosTest {
    @Test
    fun 명시적으로_seed된_query만_cache_policy로_실행한다() = runBlocking {
        val connector = DefaultFirebaseDataConnectConnector.getInstance(
            ConnectorConfig("movies", "us-central1", "movie-connector")
        )
        val operation = connector.operations.seededQuery<String, Map<String, String>>(
            operationName = "GetMovie",
            seedData = "Arrival"
        )
        val reference = operation.ref(mapOf("id" to "movie-1"))

        val result = reference.execute(DataConnectFetchPolicy.CACHE_ONLY)

        assertEquals("Arrival", result.data)
        assertEquals(DataConnectDataSource.MEMORY, result.dataSource)
        assertEquals(DataConnectFetchPolicy.CACHE_ONLY, reference.state.value.fetchPolicy)
        assertEquals(DataConnectRequestStatus.SUCCEEDED, reference.state.value.status)
    }

    @Test
    fun server_only_query는_bridge_required로_실패하고_상태를_보존한다() = runBlocking {
        val operation = GeneratedConnector.instance.operations.seededQuery<String, Unit>(
            operationName = "GetMovie",
            seedData = "Arrival"
        )
        val reference = operation.ref(Unit)

        val exception = assertFailsWith<DataConnectOperationException> {
            reference.execute(DataConnectFetchPolicy.SERVER_ONLY)
        }

        assertEquals(DataConnectFailureKind.BRIDGE_REQUIRED, exception.kind)
        assertEquals(DataConnectFetchPolicy.SERVER_ONLY, reference.state.value.fetchPolicy)
        assertEquals(DataConnectRequestStatus.UNSUPPORTED, reference.state.value.status)
    }

    @Test
    fun mutation은_성공을_가장하지_않고_bridge_required로_실패한다() = runBlocking {
        val operation = GeneratedConnector.instance.operations.mutation<Unit, Map<String, String>>(
            operationName = "AddMovie"
        )
        val reference = operation.ref(mapOf("title" to "Arrival"))

        val exception = assertFailsWith<DataConnectOperationException> { reference.execute() }

        assertEquals(DataConnectFailureKind.BRIDGE_REQUIRED, exception.kind)
        assertEquals(DataConnectRequestStatus.UNSUPPORTED, reference.state.value.status)
    }

    @Test
    fun seed가_없는_cache_query는_failed_상태를_보존한다() = runBlocking {
        val operation = GeneratedConnector.instance.operations.query<String, Unit>("GetMovie")
        val reference = operation.ref(Unit)

        val exception = assertFailsWith<DataConnectOperationException> {
            reference.execute(DataConnectFetchPolicy.CACHE_ONLY)
        }

        assertEquals(DataConnectFailureKind.OPERATION, exception.kind)
        assertEquals(DataConnectRequestStatus.FAILED, reference.state.value.status)
    }

    @Test
    fun 동일한_seed_결과도_execution_id로_각_구독_이벤트를_구분한다() = runBlocking {
        val operation = GeneratedConnector.instance.operations.seededQuery<String, Unit>(
            operationName = "GetMovie",
            seedData = "Arrival"
        )
        val reference = operation.ref(Unit)
        val events = async { reference.subscribe().take(2).toList() }
        yield()

        reference.execute(DataConnectFetchPolicy.PREFER_CACHE)
        reference.execute(DataConnectFetchPolicy.PREFER_CACHE)

        assertEquals(listOf(1L, 2L), events.await().map { it.getOrThrow().executionId })
    }

    @Test
    fun 느린_구독자가_여러_execute를_막지_않는다() = runBlocking {
        val reference = GeneratedConnector.instance.operations
            .seededQuery<String, Unit>("GetMovie", "Arrival")
            .ref(Unit)
        val firstEvent = CompletableDeferred<Unit>()
        val releaseSubscriber = CompletableDeferred<Unit>()
        val subscriber = launch {
            reference.subscribe().collect {
                firstEvent.complete(Unit)
                releaseSubscriber.await()
            }
        }
        yield()

        withTimeout(1_000) {
            reference.execute(DataConnectFetchPolicy.CACHE_ONLY)
            firstEvent.await()
            repeat(10) { reference.execute(DataConnectFetchPolicy.CACHE_ONLY) }
        }

        releaseSubscriber.complete(Unit)
        subscriber.cancelAndJoin()
    }

    @Test
    fun generic_connector가_config와_operations_factory를_copy한다() {
        val original = DefaultFirebaseDataConnectConnector.getInstance(
            ConnectorConfig("orders", "asia-northeast3", "mobile")
        )
        val copied = original.copy(
            dataConnect = zone.ien.firebase.dataconnect.FirebaseDataConnect.getInstance(
                ConnectorConfig("orders", "asia-northeast3", "admin")
            )
        )

        assertEquals("mobile", original.dataConnect.config.connector)
        assertEquals("admin", copied.dataConnect.config.connector)
        assertEquals(original.operations, copied.operations)
    }
}
