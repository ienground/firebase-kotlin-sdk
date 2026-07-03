package zone.ien.firebase.remoteconfig.interop

import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutsStateSubscriber

public class IOSFirebaseRemoteConfigInterop : FirebaseRemoteConfigInterop {
    override fun registerRolloutsStateSubscriber(namespace: String, subscriber: RolloutsStateSubscriber) {
        throw UnsupportedOperationException("RolloutsState is not supported on iOS.")
    }
}
