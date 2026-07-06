package zone.ien.firebase.encoders

import kotlin.reflect.KClass

internal actual fun <T : Annotation> T.annotationKey(): String {
    val qualifiedName = this::class.qualifiedName ?: ""
    return if (qualifiedName.contains("annotationImpl$")) {
        val pkg = qualifiedName.substringBefore("annotationImpl$")
        val rest = qualifiedName.substringAfter("annotationImpl$").substringBeforeLast("$")
        val simpleAnnName = rest.substringAfterLast("_")
        pkg + simpleAnnName
    } else {
        qualifiedName
    }
}
