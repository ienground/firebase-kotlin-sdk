package zone.ien.firebase.remoteconfig.interop.rollouts

public interface RolloutsStateSubscriber {
    public fun onRolloutsStateChanged(rolloutsState: RolloutsState)
}
