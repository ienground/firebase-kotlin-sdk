package zone.ien.firebase.encoders.processor

import kotlin.test.Test
import kotlin.test.assertTrue

class EncodersProcessorTest {

    @Test
    fun testProcessorSupportedAnnotations() {
        val processor = EncodersProcessor()
        val supportedTypes = processor.supportedAnnotationTypes
        
        // Assert processor targets the correct Encodable annotation classpath
        assertTrue(supportedTypes.contains("zone.ien.firebase.encoders.annotations.Encodable"))
    }
}
