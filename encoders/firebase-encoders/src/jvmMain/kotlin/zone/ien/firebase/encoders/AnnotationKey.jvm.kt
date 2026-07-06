package zone.ien.firebase.encoders

import kotlin.jvm.kotlin
import kotlin.reflect.KClass

internal actual fun <T : Annotation> T.annotationKey(): KClass<out Annotation> {
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    return (this as java.lang.annotation.Annotation).annotationType().kotlin
}
