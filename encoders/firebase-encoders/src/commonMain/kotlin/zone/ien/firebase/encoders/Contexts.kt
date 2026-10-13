package zone.ien.firebase.encoders

public interface ObjectEncoderContext {
    public fun add(name: String, value: Any?): ObjectEncoderContext
    public fun add(name: String, value: Double): ObjectEncoderContext
    public fun add(name: String, value: Int): ObjectEncoderContext
    public fun add(name: String, value: Long): ObjectEncoderContext
    public fun add(name: String, value: Boolean): ObjectEncoderContext
    public fun add(name: String, value: Float): ObjectEncoderContext

    public fun add(field: FieldDescriptor, value: Any?): ObjectEncoderContext
    public fun add(field: FieldDescriptor, value: Double): ObjectEncoderContext
    public fun add(field: FieldDescriptor, value: Int): ObjectEncoderContext
    public fun add(field: FieldDescriptor, value: Long): ObjectEncoderContext
    public fun add(field: FieldDescriptor, value: Boolean): ObjectEncoderContext
    public fun add(field: FieldDescriptor, value: Float): ObjectEncoderContext

    public fun inline(value: Any?): ObjectEncoderContext
    public fun nested(name: String): ObjectEncoderContext
    public fun nested(field: FieldDescriptor): ObjectEncoderContext
}

public interface ValueEncoderContext {
    public fun add(value: String?): ValueEncoderContext
    public fun add(value: Double): ValueEncoderContext
    public fun add(value: Int): ValueEncoderContext
    public fun add(value: Long): ValueEncoderContext
    public fun add(value: Boolean): ValueEncoderContext
    public fun add(value: ByteArray): ValueEncoderContext
}
