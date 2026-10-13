package zone.ien.firebase.encoders.reflective

import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import kotlin.reflect.KClass

public actual class ReflectiveObjectEncoder<T : Any> actual constructor() : ObjectEncoder<T> {

    actual override fun encode(value: T, context: ObjectEncoderContext) {
        val clazz = value::class
        @Suppress("UNCHECKED_CAST")
        val encoder = explicitEncoders[clazz] as? ObjectEncoder<T>
        
        if (encoder != null) {
            encoder.encode(value, context)
        } else {
            throw UnsupportedOperationException(
                "Reflection-based auto-serialization is not supported on iOS Native target. " +
                "Please register an explicit manual/generated encoder for class: ${clazz.simpleName} " +
                "using ReflectiveObjectEncoder.registerEncoderExplicit(...) or switch to generated encoders."
            )
        }
    }

    public actual companion object {
        private val explicitEncoders = mutableMapOf<KClass<*>, ObjectEncoder<*>>()

        public actual fun <U : Any> registerEncoderExplicit(clazz: KClass<U>, encoder: ObjectEncoder<in U>) {
            explicitEncoders[clazz] = encoder
        }
    }
}
