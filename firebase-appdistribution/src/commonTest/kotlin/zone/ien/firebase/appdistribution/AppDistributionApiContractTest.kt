package zone.ien.firebase.appdistribution

import kotlin.test.Test
import kotlin.test.assertEquals

class AppDistributionApiContractTest {
    @Test
    fun testAppDistributionTypesExposedInCommonApi() {
        assertEquals("FirebaseAppDistribution", FirebaseAppDistribution::class.simpleName)
    }
}
