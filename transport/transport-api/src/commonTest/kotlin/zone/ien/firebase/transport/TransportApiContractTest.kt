package zone.ien.firebase.transport

import kotlin.test.Test
import kotlin.test.assertEquals

class TransportApiContractTest {
    @Test
    fun testTransportApiTypesExposedInCommonApi() {
        assertEquals("Encoding", Encoding::class.simpleName)
        assertEquals("Event", Event::class.simpleName)
        assertEquals("Transport", Transport::class.simpleName)
        assertEquals("TransportFactory", TransportFactory::class.simpleName)
        assertEquals("Transformer", Transformer::class.simpleName)
    }

    @Test
    fun testPriorityProvidesAllSupportedValues() {
        assertEquals(
            listOf("DEFAULT", "VERY_LOW", "HIGHEST"),
            Priority.entries.map { it.name }
        )
    }
}
