package zone.ien.firebase.appdistribution

import kotlin.test.Test
import kotlin.test.assertEquals

class AppDistributionApiContractTest {
    @Test
    fun 앱_배포_구현이_공통_API에_노출된다() {
        assertEquals("FirebaseAppDistribution", FirebaseAppDistribution::class.simpleName)
    }
}
