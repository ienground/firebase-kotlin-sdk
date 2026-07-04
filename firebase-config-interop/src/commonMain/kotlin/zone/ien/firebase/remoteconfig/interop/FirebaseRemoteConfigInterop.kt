package zone.ien.firebase.remoteconfig.interop

import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutsStateSubscriber

public interface FirebaseRemoteConfigInterop {
    public fun registerRolloutsStateSubscriber(namespace: String, subscriber: RolloutsStateSubscriber)
}
