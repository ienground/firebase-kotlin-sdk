package zone.ien.firebase.decoders.json

import kotlin.test.Test
import kotlin.test.assertEquals

class DataDecoderApiContractTest {
    @Test
    fun testDecoderContractExposedInCommonApi() {
        assertEquals("DataDecoder", DataDecoder::class.simpleName)
        assertEquals("ObjectDecoder", ObjectDecoder::class.simpleName)
    }
}
