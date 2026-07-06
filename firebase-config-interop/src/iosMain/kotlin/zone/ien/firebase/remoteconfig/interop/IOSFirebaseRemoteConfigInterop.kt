package zone.ien.firebase.remoteconfig.interop

import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutsStateSubscriber

import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutsState

public class IOSFirebaseRemoteConfigInterop : FirebaseRemoteConfigInterop {
    private val lock = platform.Foundation.NSLock()
    private val subscribers = mutableMapOf<String, MutableList<RolloutsStateSubscriber>>()
    override fun registerRolloutsStateSubscriber(namespace: String, subscriber: RolloutsStateSubscriber) {
        lock.lock()
        try {
            subscribers.getOrPut(namespace) { mutableListOf() }.add(subscriber)
        } finally {
            lock.unlock()
        }

        // Initial empty state notification to satisfy compilation & call flow
        val initialDummyState = RolloutsState(emptySet())
        subscriber.onRolloutsStateChanged(initialDummyState)
    }
}