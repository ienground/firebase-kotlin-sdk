package zone.ien.firebase.transport

public expect interface Transformer<T, U> {
    public fun apply(input: T): U
}
