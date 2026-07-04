package zone.ien.firebase.encoders.config

import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ValueEncoder
import kotlin.reflect.KClass

public interface EncoderConfig<T : EncoderConfig<T>> {
    public fun <U : Any> registerEncoder(clazz: KClass<U>, encoder: ObjectEncoder<in U>): T
    public fun <U : Any> registerEncoder(clazz: KClass<U>, encoder: ValueEncoder<in U>): T
}

public interface Configurator {
    public fun configure(config: EncoderConfig<*>)
}
