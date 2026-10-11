package zone.ien.firebase.transport

public expect class Encoding {
    public val name: String

    public companion object {
        public fun of(name: String): Encoding
    }
}
