package zone.ien.firebase.decoders.json

import kotlin.test.Test
import kotlin.test.assertEquals

class DataDecoderApiContractTest {
    @Test
    fun 디코더_계약이_공통_API에_노출된다() {
        assertEquals("DataDecoder", DataDecoder::class.simpleName)
        assertEquals("ObjectDecoder", ObjectDecoder::class.simpleName)
    }
}
