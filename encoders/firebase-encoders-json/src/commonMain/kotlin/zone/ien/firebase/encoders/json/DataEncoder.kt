package zone.ien.firebase.encoders.json

public interface DataEncoder {
    public fun encode(value: Any): String
    public fun encode(value: Any, appendable: Appendable)
}
