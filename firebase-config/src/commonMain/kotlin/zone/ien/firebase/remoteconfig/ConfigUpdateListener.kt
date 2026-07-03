package zone.ien.firebase.remoteconfig

public interface ConfigUpdateListener {
    public fun onUpdate(configUpdate: ConfigUpdate)
    public fun onError(error: Exception)
}
