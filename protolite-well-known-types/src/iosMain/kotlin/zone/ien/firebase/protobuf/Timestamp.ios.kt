package zone.ien.firebase.protobuf

public actual class Timestamp private constructor(
    public actual val seconds: Long,
    public actual val nanos: Int
) {
    private actual constructor() : this(0L, 0)

    public actual companion object {
        public actual fun newBuilder(): Builder = Builder()
    }

    public actual class Builder {
        private var seconds: Long = 0L
        private var nanos: Int = 0

        public actual fun setSeconds(value: Long): Builder {
            this.seconds = value
            return this
        }

        public actual fun setNanos(value: Int): Builder {
            this.nanos = value
            return this
        }

        public actual fun build(): Timestamp = Timestamp(seconds, nanos)
    }
}
