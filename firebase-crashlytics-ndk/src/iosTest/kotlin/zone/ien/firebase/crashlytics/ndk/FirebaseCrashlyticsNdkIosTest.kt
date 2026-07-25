package zone.ien.firebase.crashlytics.ndk

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertSame

class FirebaseCrashlyticsNdkIosTest {
    @Test
    fun iOS에서는_NDK_크래시_수집을_지원하지_않는다() {
        assertFalse(FirebaseCrashlyticsNdk.getInstance().isNdkCrashCaptureEnabled())
    }

    @Test
    fun 동일한_싱글턴_인스턴스를_반환한다() {
        assertSame(FirebaseCrashlyticsNdk.getInstance(), FirebaseCrashlyticsNdk.getInstance())
    }
}
