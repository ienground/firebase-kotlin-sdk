package zone.ien.firebase.protobuf

public actual class Timestamp private constructor(
    private val androidTimestamp: com.google.protobuf.Timestamp
) {
    public actual val seconds: Long
        get() = androidTimestamp.seconds

    public actual val nanos: Int
        get() = androidTimestamp.nanos

    public actual companion object {
        public actual fun newBuilder(): Builder = Builder(com.google.protobuf.Timestamp.newBuilder())
    }

    public actual class Builder internal constructor(
        private val androidBuilder: com.google.protobuf.Timestamp.Builder
    ) {
        public actual fun setSeconds(value: Long): Builder {
            androidBuilder.setSeconds(value)
            return this
        }

        public actual fun setNanos(value: Int): Builder {
            androidBuilder.setNanos(value)
            return this
        }

        public actual fun build(): Timestamp = Timestamp(androidBuilder.build())
    }
}
