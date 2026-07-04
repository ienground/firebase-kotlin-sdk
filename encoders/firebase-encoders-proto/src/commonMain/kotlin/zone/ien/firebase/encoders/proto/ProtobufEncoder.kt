package zone.ien.firebase.encoders.proto

import zone.ien.firebase.encoders.FieldDescriptor
import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import zone.ien.firebase.encoders.ValueEncoder
import zone.ien.firebase.encoders.ValueEncoderContext
import zone.ien.firebase.encoders.config.EncoderConfig
import kotlin.reflect.KClass

public class ProtobufEncoder : EncoderConfig<ProtobufEncoder> {
    private val objectEncoders = mutableMapOf<KClass<*>, ObjectEncoder<*>>()
    private val valueEncoders = mutableMapOf<KClass<*>, ValueEncoder<*>>()
    private var fallbackEncoder: ValueEncoder<Any>? = null

    override fun <U : Any> registerEncoder(clazz: KClass<U>, encoder: ObjectEncoder<in U>): ProtobufEncoder {
        objectEncoders[clazz] = encoder
        return this
    }

    override fun <U : Any> registerEncoder(clazz: KClass<U>, encoder: ValueEncoder<in U>): ProtobufEncoder {
        valueEncoders[clazz] = encoder
        return this
    }

    public fun registerFallbackEncoder(fallback: ValueEncoder<Any>): ProtobufEncoder {
        this.fallbackEncoder = fallback
        return this
    }

    public fun encode(value: Any): ByteArray {
        val sink = BinarySink()
        encode(value, sink)
        return sink.toByteArray()
    }

    public fun encode(value: Any, sink: BinarySink) {
        val context = ProtobufDataEncoderContext(
            sink,
            objectEncoders,
            valueEncoders,
            fallbackEncoder
        )
        context.internalEncode(value)
    }
}

public class BinarySink {
    private val buffer = mutableListOf<Byte>()

    public fun writeByte(b: Byte) {
        buffer.add(b)
    }

    public fun writeBytes(bytes: ByteArray) {
        for (b in bytes) {
            buffer.add(b)
        }
    }

    public fun writeVarint32(value: Int) {
        var temp = value
        if (temp < 0) {
            writeVarint64(temp.toLong())
            return
        }
        while (true) {
            if ((temp and 0x7F.inv()) == 0) {
                writeByte(temp.toByte())
                return
            } else {
                writeByte(((temp and 0x7F) or 0x80).toByte())
                temp = temp ushr 7
            }
        }
    }

    public fun writeVarint64(value: Long) {
        var temp = value
        while (true) {
            if ((temp and 0x7F.inv().toLong()) == 0L) {
                writeByte(temp.toByte())
                return
            } else {
                writeByte(((temp.toInt() and 0x7F) or 0x80).toByte())
                temp = temp ushr 7
            }
        }
    }

    public fun toByteArray(): ByteArray = buffer.toByteArray()
}

private class ProtobufDataEncoderContext(
    private val sink: BinarySink,
    private val objectEncoders: Map<KClass<*>, ObjectEncoder<*>>,
    private val valueEncoders: Map<KClass<*>, ValueEncoder<*>>,
    private val fallbackEncoder: ValueEncoder<Any>?
) : ObjectEncoderContext, ValueEncoderContext {

    private var currentFieldTag: Int = 0
    private var currentFieldEncoding: IntEncoding = IntEncoding.DEFAULT

    fun internalEncode(value: Any?) {
        if (value == null) return

        when (value) {
            is String -> {
                val bytes = value.encodeToByteArray()
                sink.writeVarint32(bytes.size)
                sink.writeBytes(bytes)
            }
            is Boolean -> {
                sink.writeByte(if (value) 1 else 0)
            }
            is Int -> {
                encodeInt(value)
            }
            is Long -> {
                encodeLong(value)
            }
            is Float -> {
                // fixed 32-bit float encoding
                val bits = value.toRawBits()
                sink.writeByte((bits and 0xFF).toByte())
                sink.writeByte(((bits ushr 8) and 0xFF).toByte())
                sink.writeByte(((bits ushr 16) and 0xFF).toByte())
                sink.writeByte(((bits ushr 24) and 0xFF).toByte())
            }
            is Double -> {
                // fixed 64-bit double encoding
                val bits = value.toRawBits()
                sink.writeByte((bits and 0xFF).toByte())
                sink.writeByte(((bits ushr 8) and 0xFF).toByte())
                sink.writeByte(((bits ushr 16) and 0xFF).toByte())
                sink.writeByte(((bits ushr 24) and 0xFF).toByte())
                sink.writeByte(((bits ushr 32) and 0xFF).toByte())
                sink.writeByte(((bits ushr 40) and 0xFF).toByte())
                sink.writeByte(((bits ushr 48) and 0xFF).toByte())
                sink.writeByte(((bits ushr 56) and 0xFF).toByte())
            }
            is ByteArray -> {
                sink.writeVarint32(value.size)
                sink.writeBytes(value)
            }
            is Collection<*> -> {
                // Encode collection items as repeated fields
                val savedTag = currentFieldTag
                val savedEnc = currentFieldEncoding
                for (item in value) {
                    if (item == null) continue
                    // Write field header tag for each item
                    writeTagHeader(savedTag, getWireType(item))
                    val subContext = ProtobufDataEncoderContext(sink, objectEncoders, valueEncoders, fallbackEncoder)
                    subContext.currentFieldTag = savedTag
                    subContext.currentFieldEncoding = savedEnc
                    subContext.internalEncode(item)
                }
            }
            else -> {
                val objClass = value::class
                val objEncoder = objectEncoders[objClass]
                if (objEncoder != null) {
                    // Nested message needs length prefix serialization
                    val subSink = BinarySink()
                    val subContext = ProtobufDataEncoderContext(subSink, objectEncoders, valueEncoders, fallbackEncoder)
                    @Suppress("UNCHECKED_CAST")
                    (objEncoder as ObjectEncoder<Any>).encode(value, subContext)
                    val bytes = subSink.toByteArray()
                    sink.writeVarint32(bytes.size)
                    sink.writeBytes(bytes)
                } else {
                    val valEncoder = valueEncoders[objClass]
                    if (valEncoder != null) {
                        @Suppress("UNCHECKED_CAST")
                        (valEncoder as ValueEncoder<Any>).encode(value, this)
                    } else if (fallbackEncoder != null) {
                        fallbackEncoder.encode(value, this)
                    }
                }
            }
        }
    }

    private fun encodeInt(value: Int) {
        when (currentFieldEncoding) {
            IntEncoding.DEFAULT -> {
                sink.writeVarint32(value)
            }
            IntEncoding.SIGNED -> {
                // Zigzag encoding: (n << 1) ^ (n >> 31)
                sink.writeVarint32((value shl 1) xor (value shr 31))
            }
            IntEncoding.FIXED -> {
                sink.writeByte((value and 0xFF).toByte())
                sink.writeByte(((value ushr 8) and 0xFF).toByte())
                sink.writeByte(((value ushr 16) and 0xFF).toByte())
                sink.writeByte(((value ushr 24) and 0xFF).toByte())
            }
        }
    }

    private fun encodeLong(value: Long) {
        when (currentFieldEncoding) {
            IntEncoding.DEFAULT -> {
                sink.writeVarint64(value)
            }
            IntEncoding.SIGNED -> {
                // Zigzag encoding: (n << 1) ^ (n >> 63)
                sink.writeVarint64((value shl 1) xor (value shr 63))
            }
            IntEncoding.FIXED -> {
                sink.writeByte((value and 0xFFL).toByte())
                sink.writeByte(((value ushr 8) and 0xFFL).toByte())
                sink.writeByte(((value ushr 16) and 0xFFL).toByte())
                sink.writeByte(((value ushr 24) and 0xFFL).toByte())
                sink.writeByte(((value ushr 32) and 0xFFL).toByte())
                sink.writeByte(((value ushr 40) and 0xFFL).toByte())
                sink.writeByte(((value ushr 48) and 0xFFL).toByte())
                sink.writeByte(((value ushr 56) and 0xFFL).toByte())
            }
        }
    }

    private fun addValue(descriptor: FieldDescriptor, value: Any?): ObjectEncoderContext {
        if (value == null) return this
        val tagAnno = descriptor.getProperty(Protobuf::class) ?: return this
        val tag = tagAnno.tag
        val enc = tagAnno.intEncoding

        // Collection manages tag header formatting internally on sub-elements
        if (value !is Collection<*>) {
            writeTagHeader(tag, getWireType(value))
        }

        val subContext = ProtobufDataEncoderContext(sink, objectEncoders, valueEncoders, fallbackEncoder)
        subContext.currentFieldTag = tag
        subContext.currentFieldEncoding = enc
        subContext.internalEncode(value)
        return this
    }

    private fun getWireType(value: Any): Int {
        return when (value) {
            is Int, is Long, is Boolean -> {
                if (currentFieldEncoding == IntEncoding.FIXED) {
                    if (value is Int) 5 else 1
                } else {
                    0 // Varint
                }
            }
            is Float -> 5 // 32-bit
            is Double -> 1 // 64-bit
            is String, is ByteArray, is Collection<*>, is Map<*, *> -> 2 // Length-delimited
            else -> {
                // Nested object / custom encoder values are serialized as embedded message (length-delimited)
                2
            }
        }
    }

    private fun writeTagHeader(tag: Int, wireType: Int) {
        sink.writeVarint32((tag shl 3) or wireType)
    }

    // ObjectEncoderContext implementations
    override fun add(name: String, value: Any?): ObjectEncoderContext = this // Proto encoding targets field descriptor schemas, not ad-hoc named properties.
    override fun add(name: String, value: Double): ObjectEncoderContext = this
    override fun add(name: String, value: Int): ObjectEncoderContext = this
    override fun add(name: String, value: Long): ObjectEncoderContext = this
    override fun add(name: String, value: Boolean): ObjectEncoderContext = this
    override fun add(name: String, value: Float): ObjectEncoderContext = this

    override fun add(field: FieldDescriptor, value: Any?): ObjectEncoderContext = addValue(field, value)
    override fun add(field: FieldDescriptor, value: Double): ObjectEncoderContext = addValue(field, value)
    override fun add(field: FieldDescriptor, value: Int): ObjectEncoderContext = addValue(field, value)
    override fun add(field: FieldDescriptor, value: Long): ObjectEncoderContext = addValue(field, value)
    override fun add(field: FieldDescriptor, value: Boolean): ObjectEncoderContext = addValue(field, value)
    override fun add(field: FieldDescriptor, value: Float): ObjectEncoderContext = addValue(field, value)

    override fun inline(value: Any?): ObjectEncoderContext {
        internalEncode(value)
        return this
    }

    override fun nested(name: String): ObjectEncoderContext = this
    override fun nested(field: FieldDescriptor): ObjectEncoderContext = this

    // ValueEncoderContext implementations
    override fun add(value: String?): ValueEncoderContext {
        internalEncode(value)
        return this
    }

    override fun add(value: Double): ValueEncoderContext {
        internalEncode(value)
        return this
    }

    override fun add(value: Int): ValueEncoderContext {
        internalEncode(value)
        return this
    }

    override fun add(value: Long): ValueEncoderContext {
        internalEncode(value)
        return this
    }

    override fun add(value: Boolean): ValueEncoderContext {
        internalEncode(value)
        return this
    }

    override fun add(value: ByteArray): ValueEncoderContext {
        internalEncode(value)
        return this
    }
}
