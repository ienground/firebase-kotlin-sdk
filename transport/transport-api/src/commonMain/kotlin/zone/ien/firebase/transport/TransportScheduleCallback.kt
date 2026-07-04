package zone.ien.firebase.transport

public expect interface TransportScheduleCallback {
    public fun onSchedule(error: Exception?)
}
