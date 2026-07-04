package zone.ien.firebase.encoders.json

import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import zone.ien.firebase.encoders.ValueEncoder
import zone.ien.firebase.encoders.ValueEncoderContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonDataEncoderBuilderTest {

    data class SubMessage(val extra: String)
    data class TestMessage(
        val title: String,
        val number: Int,
        val flag: Boolean,
        val details: SubMessage?,
        val tags: List<String>
    )

    private val subMessageEncoder = object : ObjectEncoder<SubMessage> {
        override fun encode(value: SubMessage, context: ObjectEncoderContext) {
            context.add("extra", value.extra)
        }
    }

    private val testMessageEncoder = object : ObjectEncoder<TestMessage> {
        override fun encode(value: TestMessage, context: ObjectEncoderContext) {
            context.add("title", value.title)
            context.add("number", value.number)
            context.add("flag", value.flag)
            context.add("details", value.details)
            context.add("tags", value.tags)
        }
    }

    @Test
    fun testJsonSerializationWithNestedObjects() {
        val encoder = JsonDataEncoderBuilder()
            .registerEncoder(TestMessage::class, testMessageEncoder)
            .registerEncoder(SubMessage::class, subMessageEncoder)
            .build()

        val message = TestMessage(
            title = "hello\\kmp",
            number = 101,
            flag = true,
            details = SubMessage("nested_value"),
            tags = listOf("dev", "test")
        )

        val json = encoder.encode(message)

        // Assert keys and escaped characters exist in JSON string
        assertTrue(json.contains("\"title\":\"hello\\\\kmp\""))
        assertTrue(json.contains("\"number\":101"))
        assertTrue(json.contains("\"flag\":true"))
        assertTrue(json.contains("\"details\":{\"extra\":\"nested_value\"}"))
        assertTrue(json.contains("\"tags\":[\"dev\",\"test\"]"))

        // ✅ Appendable overload도 동일한 결과를 내는지 검증
        val builder = StringBuilder()
        encoder.encode(message, builder)
        assertEquals(json, builder.toString())
    }

    @Test
    fun testIgnoreNullValuesOption() {
        val encoderIgnoring = JsonDataEncoderBuilder()
            .registerEncoder(TestMessage::class, testMessageEncoder)
            .registerEncoder(SubMessage::class, subMessageEncoder)
            .ignoreNullValues(true)
            .build()

        val encoderKeeping = JsonDataEncoderBuilder()
            .registerEncoder(TestMessage::class, testMessageEncoder)
            .registerEncoder(SubMessage::class, subMessageEncoder)
            .ignoreNullValues(false)
            .build()

        val message = TestMessage(
            title = "null_test",
            number = 0,
            flag = false,
            details = null,
            tags = emptyList()
        )

        val jsonIgnoring = encoderIgnoring.encode(message)
        val jsonKeeping = encoderKeeping.encode(message)

        // IgnoreNullValues should omit "details" key entirely
        assertTrue(!jsonIgnoring.contains("\"details\""))
        
        // Keeping nulls should output "details":null
        assertTrue(jsonKeeping.contains("\"details\":null"))
    }

    @Test
    fun testFallbackEncoder() {
        val fallback = object : ValueEncoder<Any> {
            override fun encode(value: Any, context: ValueEncoderContext) {
                context.add("FALLBACK_${value}")
            }
        }

        val encoder = JsonDataEncoderBuilder()
            .registerFallbackEncoder(fallback)
            .build()

        val json = encoder.encode(99.9)
        assertEquals("\"FALLBACK_99.9\"", json)
    }
}
