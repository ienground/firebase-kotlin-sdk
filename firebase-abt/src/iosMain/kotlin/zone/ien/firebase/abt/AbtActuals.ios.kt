package zone.ien.firebase.abt

public actual class AbtException actual constructor(message: String?, cause: Throwable?) : Exception(message, cause) {
    public actual constructor(message: String?) : this(message, null)
}
