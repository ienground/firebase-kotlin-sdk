package zone.ien.firebase.remoteconfig

import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseRemoteConfigModelsTest {
    @Test
    fun testRemoteConfigSettingsBuilderUsesDefaultValues() {
        val settings = FirebaseRemoteConfigSettings.Builder().build()

        assertEquals(43_200L, settings.minimumFetchIntervalInSeconds)
        assertEquals(60L, settings.fetchTimeoutInSeconds)
    }

    @Test
    fun testRemoteConfigSettingsBuilderPreservesCustomValuesAndChaining() {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .setFetchTimeoutInSeconds(15)
            .build()

        assertEquals(0L, settings.minimumFetchIntervalInSeconds)
        assertEquals(15L, settings.fetchTimeoutInSeconds)
    }

    @Test
    fun testRemoteConfigEnumsProvideAllSupportedValues() {
        assertEquals(
            listOf("SUCCESS", "FAILURE", "THROTTLED", "NO_FETCH_YET"),
            FetchStatus.entries.map { it.name }
        )
        assertEquals(listOf("STATIC", "DEFAULT", "REMOTE"), ValueSource.entries.map { it.name })
    }
}
