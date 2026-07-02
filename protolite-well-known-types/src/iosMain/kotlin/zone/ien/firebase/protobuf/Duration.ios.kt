package zone.ien.firebase.protobuf

public actual class Duration private constructor(
    public actual val seconds: Long,
    public actual val nanos: Int
) {
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

        public actual fun build(): Duration = Duration(seconds, nanos)
    }
}
