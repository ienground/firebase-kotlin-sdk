package zone.ien.firebase.encoders.reflective

import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import kotlin.reflect.KClass

public expect class ReflectiveObjectEncoder<T : Any> : ObjectEncoder<T> {
    public constructor()

    override fun encode(value: T, context: ObjectEncoderContext)

    public companion object {
        public fun <U : Any> registerEncoderExplicit(clazz: KClass<U>, encoder: ObjectEncoder<in U>)
    }
}
