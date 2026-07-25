package zone.ien.firebase.remoteconfig.interop.rollouts

import kotlin.test.Test
import kotlin.test.assertEquals

class RolloutsModelsTest {
    @Test
    fun 할당_정보를_값으로_비교하고_복사한다() {
        val assignment = RolloutAssignment("rollout", "variant-a", "banner", "blue", 7)

        assertEquals(assignment, assignment.copy())
        assertEquals("variant-b", assignment.copy(variantId = "variant-b").variantId)
    }

    @Test
    fun 상태는_중복되지_않은_할당_집합을_보존한다() {
        val assignment = RolloutAssignment("rollout", "variant-a", "banner", "blue", 7)
        val state = RolloutsState(setOf(assignment, assignment.copy()))

        assertEquals(1, state.rolloutAssignments.size)
        assertEquals(assignment, state.rolloutAssignments.single())
    }
}
