package zone.ien.firebase.remoteconfig.interop.rollouts

public class RolloutAssignment(
    public val rolloutId: String,
    public val variantId: String,
    public val parameterKey: String,
    public val parameterValue: String,
    public val templateVersion: Long
)
