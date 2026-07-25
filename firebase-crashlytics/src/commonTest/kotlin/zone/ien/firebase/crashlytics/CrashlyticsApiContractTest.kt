package zone.ien.firebase.crashlytics

import kotlin.test.Test
import kotlin.test.assertEquals

class CrashlyticsApiContractTest {
    @Test
    fun 크래시리틱스_타입이_공통_API에_노출된다() {
        assertEquals("FirebaseCrashlytics", FirebaseCrashlytics::class.simpleName)
    }
}
