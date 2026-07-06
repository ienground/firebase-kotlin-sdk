package zone.ien.firebase.remoteconfig.interop

import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutsStateSubscriber

import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutsState

public class IOSFirebaseRemoteConfigInterop : FirebaseRemoteConfigInterop {
    private val subscribers = mutableMapOf<String, MutableList<RolloutsStateSubscriber>>()

    override fun registerRolloutsStateSubscriber(namespace: String, subscriber: RolloutsStateSubscriber) {
        subscribers.getOrPut(namespace) { mutableListOf() }.add(subscriber)
        
        // Initial empty state notification to satisfy compilation & call flow
        val initialDummyState = RolloutsState(emptySet())
        subscriber.onRolloutsStateChanged(initialDummyState)
    }
}
