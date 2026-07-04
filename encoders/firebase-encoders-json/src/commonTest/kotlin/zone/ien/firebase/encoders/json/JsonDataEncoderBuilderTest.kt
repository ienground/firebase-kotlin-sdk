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

    // Wrapper types so that encoding is routed through the registered ValueEncoder
    // instead of the built-in Number/Boolean fast path.
    data class DoubleBox(val value: Double)
    data class IntBox(val value: Int)
    data class LongBox(val value: Long)
    data class BooleanBox(val value: Boolean)

    @Test
    fun testValueEncoderWithPrimitiveOverloads() {
        // Regression test: ValueEncoderContext.add(Double/Int/Long/Boolean) must delegate
        // to the underlying encoding logic instead of recursing into themselves
        // (which previously caused a StackOverflowError).
        val doubleEncoder = object : ValueEncoder<DoubleBox> {
            override fun encode(value: DoubleBox, context: ValueEncoderContext) {
                context.add(value.value)
            }
        }
        val intEncoder = object : ValueEncoder<IntBox> {
            override fun encode(value: IntBox, context: ValueEncoderContext) {
                context.add(value.value)
            }
        }
        val longEncoder = object : ValueEncoder<LongBox> {
            override fun encode(value: LongBox, context: ValueEncoderContext) {
                context.add(value.value)
            }
        }
        val booleanEncoder = object : ValueEncoder<BooleanBox> {
            override fun encode(value: BooleanBox, context: ValueEncoderContext) {
                context.add(value.value)
            }
        }

        assertEquals(
            "3.14",
            JsonDataEncoderBuilder().registerEncoder(DoubleBox::class, doubleEncoder).build().encode(DoubleBox(3.14))
        )
        assertEquals(
            "42",
            JsonDataEncoderBuilder().registerEncoder(IntBox::class, intEncoder).build().encode(IntBox(42))
        )
        assertEquals(
            "9000000000",
            JsonDataEncoderBuilder().registerEncoder(LongBox::class, longEncoder).build().encode(LongBox(9000000000L))
        )
        assertEquals(
            "true",
            JsonDataEncoderBuilder().registerEncoder(BooleanBox::class, booleanEncoder).build().encode(BooleanBox(true))
        )
    }
}
