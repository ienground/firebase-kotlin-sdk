package zone.ien.firebase.encoders

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

annotation class ProtoTag(val id: Int)
annotation class RequiredDescriptor(val isRequired: Boolean)

class FieldDescriptorTest {

    @Test
    fun testFieldDescriptorCreation() {
        val descriptor = FieldDescriptor.of("test_field")
        assertEquals("test_field", descriptor.name)
    }

    @Test
    fun testFieldDescriptorBuilderWithProperties() {
        val descriptor = FieldDescriptor.builder("metadata_field")
            .withProperty(ProtoTag(42))
            .withProperty(RequiredDescriptor(true))
            .build()

        assertEquals("metadata_field", descriptor.name)

        val protoTag = descriptor.getProperty(ProtoTag::class)
        assertNotNull(protoTag)
        assertEquals(42, protoTag.id)

        val required = descriptor.getProperty(RequiredDescriptor::class)
        assertNotNull(required)
        assertEquals(true, required.isRequired)

        val nonExistent = descriptor.getProperty(SuppressWarnings::class)
        assertNull(nonExistent)
    }
}
