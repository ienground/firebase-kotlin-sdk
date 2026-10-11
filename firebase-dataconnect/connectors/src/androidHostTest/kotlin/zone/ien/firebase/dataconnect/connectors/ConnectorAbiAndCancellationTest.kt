package zone.ien.firebase.dataconnect.connectors

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import zone.ien.firebase.dataconnect.DataConnectFetchPolicy
import zone.ien.firebase.dataconnect.DataConnectRequestStatus
import zone.ien.firebase.dataconnect.FirebaseDataConnect

class ConnectorAbiAndCancellationTest {
    @Test
    fun 기존_connector_구현체에_operations_getter를_강제하지_않는다() {
        val abstractMethods = FirebaseDataConnectConnector::class.java.declaredMethods
            .filter { java.lang.reflect.Modifier.isAbstract(it.modifiers) }
            .map { it.name }

        assertEquals(listOf("getDataConnect"), abstractMethods)
    }

    @Test
    fun query_취소는_CANCELLED_상태로_종료하고_원본을_재전파한다() = runBlocking {
        val tracker = DataConnectRequestStateTracker("GetMovie", Unit)
        val cancellation = CancellationException("query cancelled")

        val thrown = assertFailsWith<CancellationException> {
            executeAndroidQuery(tracker, DataConnectFetchPolicy.SERVER_ONLY) { throw cancellation }
        }

        assertEquals(cancellation, thrown)
        assertEquals(DataConnectRequestStatus.CANCELLED, tracker.state.value.status)
    }

    @Test
    fun mutation_취소는_CANCELLED_상태로_종료하고_원본을_재전파한다() = runBlocking {
        val tracker = DataConnectRequestStateTracker("AddMovie", Unit)
        val cancellation = CancellationException("mutation cancelled")

        val thrown = assertFailsWith<CancellationException> {
            executeAndroidMutation(tracker) { throw cancellation }
        }

        assertEquals(cancellation, thrown)
        assertEquals(DataConnectRequestStatus.CANCELLED, tracker.state.value.status)
    }
}

private class LegacyConnector(
    override val dataConnect: FirebaseDataConnect
) : FirebaseDataConnectConnector
