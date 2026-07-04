package zone.ien.firebase.encoders

import kotlin.reflect.KClass

public class FieldDescriptor private constructor(
    public val name: String,
    private val properties: Map<KClass<out Annotation>, Annotation>
) {
    public fun <T : Annotation> getProperty(clazz: KClass<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return properties[clazz] as? T
    }

    public companion object {
        public fun of(name: String): FieldDescriptor {
            return FieldDescriptor(name, emptyMap())
        }

        public fun builder(name: String): Builder {
            return Builder(name)
        }
    }

    public class Builder(private val name: String) {
        private var properties: MutableMap<KClass<out Annotation>, Annotation>? = null

        public fun <T : Annotation> withProperty(property: T): Builder {
            if (properties == null) {
                properties = mutableMapOf()
            }
            properties?.put(property::class, property)
            return this
        }

        public fun build(): FieldDescriptor {
            return FieldDescriptor(
                name,
                properties?.toMap() ?: emptyMap()
            )
        }
    }
}
