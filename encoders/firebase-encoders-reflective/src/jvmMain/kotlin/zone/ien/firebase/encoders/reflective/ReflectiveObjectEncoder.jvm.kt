package zone.ien.firebase.encoders.reflective

import zone.ien.firebase.encoders.FieldDescriptor
import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

public actual class ReflectiveObjectEncoder<T : Any> actual constructor() : ObjectEncoder<T> {

    actual override fun encode(value: T, context: ObjectEncoderContext) {
        val kotlinClazz = value::class
        @Suppress("UNCHECKED_CAST")
        val explicitEncoder = explicitEncoders[kotlinClazz] as? ObjectEncoder<T>
        if (explicitEncoder != null) {
            explicitEncoder.encode(value, context)
            return
        }

        val clazz = value::class.java
        val fields: Array<Field> = clazz.declaredFields

        for (field in fields) {
            val modifiers = field.modifiers
            if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || field.isSynthetic) {
                continue
            }
            try {
                field.isAccessible = true
                val fieldValue = field.get(value)
                val fieldName = field.name

                // Construct a simple descriptor using naming selector
                val descriptor = FieldDescriptor.of(fieldName)
                context.add(descriptor, fieldValue)
            } catch (e: Exception) {
                // Ignore accessibility or extraction failures silently
            }
        }
    }

    public actual companion object {
        private val explicitEncoders = ConcurrentHashMap<KClass<*>, ObjectEncoder<*>>()

        public actual fun <U : Any> registerEncoderExplicit(clazz: KClass<U>, encoder: ObjectEncoder<in U>) {
            explicitEncoders[clazz] = encoder
        }
    }
}
