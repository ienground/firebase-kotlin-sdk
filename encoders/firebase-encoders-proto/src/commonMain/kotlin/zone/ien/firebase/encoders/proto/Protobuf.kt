package zone.ien.firebase.encoders.proto

public enum class IntEncoding {
    DEFAULT,
    SIGNED,
    FIXED
}

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
public annotation class Protobuf(
    val tag: Int,
    val intEncoding: IntEncoding = IntEncoding.DEFAULT
)
