package zone.ien.firebase.encoders

import kotlin.reflect.KClass

internal actual fun <T : Annotation> T.annotationKey(): KClass<out Annotation> {
    return this::class
}
