package zone.ien.firebase.transport

import kotlin.test.Test
import kotlin.test.assertEquals

class TransportApiContractTest {
    @Test
    fun 전송_API_타입이_공통에_노출된다() {
        assertEquals("Encoding", Encoding::class.simpleName)
        assertEquals("Event", Event::class.simpleName)
        assertEquals("Transport", Transport::class.simpleName)
        assertEquals("TransportFactory", TransportFactory::class.simpleName)
        assertEquals("Transformer", Transformer::class.simpleName)
    }

    @Test
    fun 우선순위는_지원하는_항목을_모두_제공한다() {
        assertEquals(
            listOf("DEFAULT", "VERY_LOW", "HIGHEST"),
            Priority.entries.map { it.name }
        )
    }
}
