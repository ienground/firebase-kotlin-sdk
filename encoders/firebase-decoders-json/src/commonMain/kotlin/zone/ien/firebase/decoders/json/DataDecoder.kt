package zone.ien.firebase.decoders.json

import kotlin.reflect.KClass

public interface DataDecoder {
    public fun <T : Any> decode(json: String, clazz: KClass<T>): T
}
public inline fun <reified T : Any> DataDecoder.decode(json: String): T {
    return decode(json, T::class)
}

public interface ObjectDecoder<T> {
    public fun decode(source: Map<String, Any?>): T
}
