package zone.ien.firebase.crashlytics

import kotlin.test.Test
import kotlin.test.assertEquals

class CrashlyticsApiContractTest {
    @Test
    fun testCrashlyticsTypesExposedInCommonApi() {
        assertEquals("FirebaseCrashlytics", FirebaseCrashlytics::class.simpleName)
    }
}
