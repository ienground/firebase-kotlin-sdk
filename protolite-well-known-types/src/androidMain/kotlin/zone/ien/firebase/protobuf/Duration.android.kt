package zone.ien.firebase.protobuf

public actual class Duration private constructor(
    private val androidDuration: com.google.protobuf.Duration
) {
    public actual val seconds: Long
        get() = androidDuration.seconds

    public actual val nanos: Int
        get() = androidDuration.nanos

    public actual companion object {
        public actual fun newBuilder(): Builder = Builder(com.google.protobuf.Duration.newBuilder())
    }

    public actual class Builder internal constructor(
        private val androidBuilder: com.google.protobuf.Duration.Builder
    ) {
        public actual fun setSeconds(value: Long): Builder {
            androidBuilder.setSeconds(value)
            return this
        }

        public actual fun setNanos(value: Int): Builder {
            androidBuilder.setNanos(value)
            return this
        }

        public actual fun build(): Duration = Duration(androidBuilder.build())
    }
}
