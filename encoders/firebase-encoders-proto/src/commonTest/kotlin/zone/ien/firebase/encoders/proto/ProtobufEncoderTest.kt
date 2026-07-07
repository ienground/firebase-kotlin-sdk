package zone.ien.firebase.encoders.proto

import zone.ien.firebase.encoders.FieldDescriptor
import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProtobufEncoderTest {

    data class SubMessage(val extra: String)
    data class TestMessage(
        val title: String,
        val number: Int,
        val details: SubMessage,
        val tags: List<String>
    )

    private val subMessageEncoder = object : ObjectEncoder<SubMessage> {
        private val extraField = FieldDescriptor.builder("extra")
            .withProperty(Protobuf(tag = 1, intEncoding = IntEncoding.DEFAULT))
            .build()

        override fun encode(value: SubMessage, context: ObjectEncoderContext) {
            context.add(extraField, value.extra)
        }
    }

    private val testMessageEncoder = object : ObjectEncoder<TestMessage> {
        private val titleField = FieldDescriptor.builder("title")
            .withProperty(Protobuf(tag = 1, intEncoding = IntEncoding.DEFAULT))
            .build()
        private val numberField = FieldDescriptor.builder("number")
            .withProperty(Protobuf(tag = 2, intEncoding = IntEncoding.DEFAULT))
            .build()
        private val detailsField = FieldDescriptor.builder("details")
            .withProperty(Protobuf(tag = 3, intEncoding = IntEncoding.DEFAULT))
            .build()
        private val tagsField = FieldDescriptor.builder("tags")
            .withProperty(Protobuf(tag = 4, intEncoding = IntEncoding.DEFAULT))
            .build()

        override fun encode(value: TestMessage, context: ObjectEncoderContext) {
            context.add(titleField, value.title)
            context.add(numberField, value.number)
            context.add(detailsField, value.details)
            context.add(tagsField, value.tags)
        }
    }

    @Test
    fun testProtobufSerialization() {
        val encoder = ProtobufEncoder()
            .registerEncoder(TestMessage::class, testMessageEncoder)
            .registerEncoder(SubMessage::class, subMessageEncoder)

        val message = TestMessage(
            title = "hello",
            number = 150,
            details = SubMessage("nested"),
            tags = listOf("a", "b")
        )

        val bytes = encoder.encode(message)

        // Verifying parts of the byte array:
        // Tag 1 (title: "hello") -> WireType 2 -> (1 shl 3) | 2 = 10 (0x0A)
        // Length 5, then "hello" bytes
        assertEquals(0x0A.toByte(), bytes[0])
        assertEquals(5.toByte(), bytes[1])
        assertEquals('h'.toByte(), bytes[2])

        // Tag 2 (number: 150) -> WireType 0 -> (2 shl 3) | 0 = 16 (0x10)
        // 150 as varint -> 150 = 0x96, 0x01
        var numberTagIdx = -1
        for (i in bytes.indices) {
            if (bytes[i] == 0x10.toByte() && bytes[i + 1] == 0x96.toByte() && bytes[i + 2] == 0x01.toByte()) {
                numberTagIdx = i
                break
            }
        }
        assertTrue(numberTagIdx != -1, "Should serialize number 150 as correct varint bytes")

        // Tag 3 (details: SubMessage) -> WireType 2 -> (3 shl 3) | 2 = 26 (0x1A)
        // Details nested content is {"extra": "nested"}
        // Tag 1 (extra: "nested") -> (1 shl 3) | 2 = 10 (0x0A), length 6, "nested" -> total 8 bytes
        // Hence details size = 8.
        var detailsTagIdx = -1
        for (i in bytes.indices) {
            if (bytes[i] == 0x1A.toByte() && bytes[i + 1] == 8.toByte()) {
                detailsTagIdx = i
                break
            }
        }
        assertTrue(detailsTagIdx != -1, "Should serialize nested object with length prefix 8")
    }
}
