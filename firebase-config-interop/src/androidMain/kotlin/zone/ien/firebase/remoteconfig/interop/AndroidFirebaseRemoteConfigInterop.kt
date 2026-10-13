package zone.ien.firebase.remoteconfig.interop

import com.google.firebase.remoteconfig.interop.FirebaseRemoteConfigInterop as AndroidRemoteConfigInterop
import com.google.firebase.remoteconfig.interop.rollouts.RolloutsStateSubscriber as AndroidRolloutsStateSubscriber
import com.google.firebase.remoteconfig.interop.rollouts.RolloutsState as AndroidRolloutsState
import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutsStateSubscriber
import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutsState
import zone.ien.firebase.remoteconfig.interop.rollouts.RolloutAssignment

public class AndroidFirebaseRemoteConfigInterop(
    internal val androidInterop: AndroidRemoteConfigInterop
) : FirebaseRemoteConfigInterop {
    override fun registerRolloutsStateSubscriber(namespace: String, subscriber: RolloutsStateSubscriber) {
        val androidSubscriber = AndroidRolloutsStateSubscriber { androidRolloutsState: AndroidRolloutsState ->
            val mappedAssignments = androidRolloutsState.rolloutAssignments.map { assignment ->
                RolloutAssignment(
                    rolloutId = assignment.rolloutId,
                    variantId = assignment.variantId,
                    parameterKey = assignment.parameterKey,
                    parameterValue = assignment.parameterValue,
                    templateVersion = assignment.templateVersion
                )
            }.toSet()
            subscriber.onRolloutsStateChanged(RolloutsState(mappedAssignments))
        }
        androidInterop.registerRolloutsStateSubscriber(namespace, androidSubscriber)
    }
}
