package zone.ien.firebase

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is for Firebase SDK Internal. DO NOT use directly in outside."
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR)
public annotation class InternalFirebaseApi