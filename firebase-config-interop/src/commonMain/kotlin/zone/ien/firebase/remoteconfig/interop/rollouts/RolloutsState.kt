package zone.ien.firebase.remoteconfig.interop.rollouts

public data class RolloutsState(
    public val rolloutAssignments: Set<RolloutAssignment>
)
