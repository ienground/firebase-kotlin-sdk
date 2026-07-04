package zone.ien.firebase.transport

public expect interface Transport<T> {
    public fun send(event: Event<T>)
    public fun schedule(event: Event<T>, callback: TransportScheduleCallback)
}
