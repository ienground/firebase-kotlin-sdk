package zone.ien.firebase.remoteconfig

import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseRemoteConfigModelsTest {
    @Test
    fun 설정_빌더는_기본값을_사용한다() {
        val settings = FirebaseRemoteConfigSettings.Builder().build()

        assertEquals(43_200L, settings.minimumFetchIntervalInSeconds)
        assertEquals(60L, settings.fetchTimeoutInSeconds)
    }

    @Test
    fun 설정_빌더는_지정값과_연쇄_호출을_보존한다() {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .setFetchTimeoutInSeconds(15)
            .build()

        assertEquals(0L, settings.minimumFetchIntervalInSeconds)
        assertEquals(15L, settings.fetchTimeoutInSeconds)
    }

    @Test
    fun 상태와_값_출처는_지원하는_항목을_모두_제공한다() {
        assertEquals(
            listOf("SUCCESS", "FAILURE", "THROTTLED", "NO_FETCH_YET"),
            FetchStatus.entries.map { it.name }
        )
        assertEquals(listOf("STATIC", "DEFAULT", "REMOTE"), ValueSource.entries.map { it.name })
    }
}
