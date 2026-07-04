package zone.ien.firebase.encoders

public interface Encoder<TValue, TContext> {
    public fun encode(value: TValue, context: TContext)
}

public interface ObjectEncoder<T> : Encoder<T, ObjectEncoderContext>

public interface ValueEncoder<T> : Encoder<T, ValueEncoderContext>
