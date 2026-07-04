package zone.ien.firebase.transport

public class Encoding private constructor(public val name: String) {
    public companion object {
        public fun of(name: String): Encoding = Encoding(name)
    }
}
