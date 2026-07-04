package zone.ien.firebase.encoders.json

import zone.ien.firebase.encoders.Encoder
import zone.ien.firebase.encoders.FieldDescriptor
import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import zone.ien.firebase.encoders.ValueEncoder
import zone.ien.firebase.encoders.ValueEncoderContext
import zone.ien.firebase.encoders.config.EncoderConfig
import kotlin.reflect.KClass

public class JsonDataEncoderBuilder : EncoderConfig<JsonDataEncoderBuilder> {
    private val objectEncoders = mutableMapOf<KClass<*>, ObjectEncoder<*>>()
    private val valueEncoders = mutableMapOf<KClass<*>, ValueEncoder<*>>()
    private var fallbackEncoder: ValueEncoder<Any>? = null
    private var ignoreNullValues: Boolean = false

    public fun registerFallbackEncoder(fallbackEncoder: ValueEncoder<Any>): JsonDataEncoderBuilder {
        this.fallbackEncoder = fallbackEncoder
        return this
    }

    public fun ignoreNullValues(ignore: Boolean): JsonDataEncoderBuilder {
        this.ignoreNullValues = ignore
        return this
    }

    override fun <U : Any> registerEncoder(clazz: KClass<U>, encoder: ObjectEncoder<in U>): JsonDataEncoderBuilder {
        objectEncoders[clazz] = encoder
        return this
    }

    override fun <U : Any> registerEncoder(clazz: KClass<U>, encoder: ValueEncoder<in U>): JsonDataEncoderBuilder {
        valueEncoders[clazz] = encoder
        return this
    }

    public fun build(): DataEncoder {
        return object : DataEncoder {
            override fun encode(value: Any): String {
                val sb = StringBuilder()
                encode(value, sb)
                return sb.toString()
            }

            override fun encode(value: Any, appendable: Appendable) {
                val context = JsonValueObjectEncoderContext(
                    appendable,
                    objectEncoders,
                    valueEncoders,
                    fallbackEncoder,
                    ignoreNullValues
                )
                context.encode(value)
            }
        }
    }
}

private class JsonValueObjectEncoderContext(
    private val appendable: Appendable,
    private val objectEncoders: Map<KClass<*>, ObjectEncoder<*>>,
    private val valueEncoders: Map<KClass<*>, ValueEncoder<*>>,
    private val fallbackEncoder: ValueEncoder<Any>?,
    private val ignoreNullValues: Boolean
) : ObjectEncoderContext, ValueEncoderContext {

    private var first = true

    fun encode(value: Any?) {
        if (value == null) {
            appendable.append("null")
            return
        }

        when (value) {
            is String -> {
                appendable.append("\"").append(escape(value)).append("\"")
            }
            is Boolean -> {
                appendable.append(value.toString())
            }
            is Double -> {
                require(value.isFinite()) {
                    "NaN and Infinity are not valid JSON numbers: $value"
                }
                appendable.append(value.toString())
            }
            is Float -> {
                require(value.isFinite()) {
                    "NaN and Infinity are not valid JSON numbers: $value"
                }
                appendable.append(value.toString())
            }
            is Number -> {
                appendable.append(value.toString())
            }
            is Collection<*> -> {
                appendable.append("[")
                var firstItem = true
                for (item in value) {
                    if (!firstItem) appendable.append(",")
                    firstItem = false
                    val subContext = JsonValueObjectEncoderContext(appendable, objectEncoders, valueEncoders, fallbackEncoder, ignoreNullValues)
                    subContext.encode(item)
                }
                appendable.append("]")
            }
            is Map<*, *> -> {
                appendable.append("{")
                var firstEntry = true
                for ((key, item) in value) {
                    if (ignoreNullValues && item == null) continue
                    if (!firstEntry) appendable.append(",")
                    firstEntry = false
                    appendable.append("\"").append(escape(key.toString())).append("\":")
                    val subContext = JsonValueObjectEncoderContext(appendable, objectEncoders, valueEncoders, fallbackEncoder, ignoreNullValues)
                    subContext.encode(item)
                }
                appendable.append("}")
            }
            is ByteArray -> {
                // Encode ByteArray as simple list representation for cross-platform alignment
                appendable.append("[")
                for (i in value.indices) {
                    if (i > 0) appendable.append(",")
                    appendable.append(value[i].toString())
                }
                appendable.append("]")
            }
            else -> {
                val objClass = value::class
                val objEncoder = objectEncoders[objClass]
                if (objEncoder != null) {
                    appendable.append("{")
                    @Suppress("UNCHECKED_CAST")
                    (objEncoder as ObjectEncoder<Any>).encode(value, this)
                    appendable.append("}")
                } else {
                    val valEncoder = valueEncoders[objClass]
                    if (valEncoder != null) {
                        @Suppress("UNCHECKED_CAST")
                        (valEncoder as ValueEncoder<Any>).encode(value, this)
                    } else if (fallbackEncoder != null) {
                        fallbackEncoder.encode(value, this)
                    } else {
                        // Default fallback serialize name as string
                        appendable.append("\"").append(escape(value.toString())).append("\"")
                    }
                }
            }
        }
    }

    override fun add(name: String, value: Any?): ObjectEncoderContext {
        return addValue(name, value)
    }

    private fun addValue(name: String, value: Any?): ObjectEncoderContext {
        if (ignoreNullValues && value == null) return this
        writeKey(name)
        val subContext = JsonValueObjectEncoderContext(appendable, objectEncoders, valueEncoders, fallbackEncoder, ignoreNullValues)
        subContext.encode(value)
        return this
    }

    override fun add(name: String, value: Double): ObjectEncoderContext = addValue(name, value)
    override fun add(name: String, value: Int): ObjectEncoderContext = addValue(name, value)
    override fun add(name: String, value: Long): ObjectEncoderContext = addValue(name, value)
    override fun add(name: String, value: Boolean): ObjectEncoderContext = addValue(name, value)
    override fun add(name: String, value: Float): ObjectEncoderContext = addValue(name, value)

    override fun add(field: FieldDescriptor, value: Any?): ObjectEncoderContext = addValue(field.name, value)
    override fun add(field: FieldDescriptor, value: Double): ObjectEncoderContext = addValue(field.name, value)
    override fun add(field: FieldDescriptor, value: Int): ObjectEncoderContext = addValue(field.name, value)
    override fun add(field: FieldDescriptor, value: Long): ObjectEncoderContext = addValue(field.name, value)
    override fun add(field: FieldDescriptor, value: Boolean): ObjectEncoderContext = addValue(field.name, value)
    override fun add(field: FieldDescriptor, value: Float): ObjectEncoderContext = addValue(field.name, value)

    override fun inline(value: Any?): ObjectEncoderContext {
        encode(value)
        return this
    }

    override fun nested(name: String): ObjectEncoderContext {
        // Nested boundary verification: output is simply mapped sequentially
        writeKey(name)
        return this
    }

    override fun nested(field: FieldDescriptor): ObjectEncoderContext = nested(field.name)

    // ValueEncoderContext implementations
    override fun add(value: String?): ValueEncoderContext {
        encode(value)
        return this
    }

    override fun add(value: Double): ValueEncoderContext { encode(value); return this }
    override fun add(value: Int): ValueEncoderContext { encode(value); return this }
    override fun add(value: Long): ValueEncoderContext { encode(value); return this }
    override fun add(value: Boolean): ValueEncoderContext { encode(value); return this }
    override fun add(value: ByteArray): ValueEncoderContext { encode(value); return this }

    private fun writeKey(name: String) {
        if (!first) appendable.append(",")
        first = false
        appendable.append("\"").append(escape(name)).append("\":")
    }

    private fun escape(input: String): String {
        val sb = StringBuilder()
        for (ch in input) {
            when (ch) {
                '\"' -> sb.append("\\\"")
                '\\' -> sb.append("\\\\")
                '/' -> sb.append("\\/")
                '\b' -> sb.append("\\b")
                '\n' -> sb.append("\\n")
                '\r' -> sb.append("\\r")
                '\t' -> sb.append("\\t")
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }
}
