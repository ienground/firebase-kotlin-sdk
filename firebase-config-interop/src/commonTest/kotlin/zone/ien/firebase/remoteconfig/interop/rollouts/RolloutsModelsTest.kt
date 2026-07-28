package zone.ien.firebase.remoteconfig.interop.rollouts

import kotlin.test.Test
import kotlin.test.assertEquals

class RolloutsModelsTest {
    @Test
    fun testRolloutAssignmentEqualityAndCopy() {
        val assignment = RolloutAssignment("rollout", "variant-a", "banner", "blue", 7)

        assertEquals(assignment, assignment.copy())
        assertEquals("variant-b", assignment.copy(variantId = "variant-b").variantId)
    }

    @Test
    fun testRolloutStatePreservesUniqueAssignmentSet() {
        val assignment = RolloutAssignment("rollout", "variant-a", "banner", "blue", 7)
        val state = RolloutsState(setOf(assignment, assignment.copy()))

        assertEquals(1, state.rolloutAssignments.size)
        assertEquals(assignment, state.rolloutAssignments.single())
    }
}
