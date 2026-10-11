package zone.ien.firebase.protobuf

public expect class Timestamp private constructor() {
    public val seconds: Long
    public val nanos: Int

    public companion object {
        public fun newBuilder(): Builder
    }

    public class Builder {
        public fun setSeconds(value: Long): Builder
        public fun setNanos(value: Int): Builder
        public fun build(): Timestamp
    }
}
