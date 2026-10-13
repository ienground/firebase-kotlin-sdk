package zone.ien.firebase.installations

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue
import zone.ien.firebase.installations.interop.FidListener
import zone.ien.firebase.installations.interop.InstallationTokenResult as InteropTokenResult

class InstallationTokenResultTest {
    @Test
    fun testInstallationTokenResultPreservesTokenAndImplementsContract() {
        val result: InteropTokenResult = InstallationTokenResult(
            token = "token",
            tokenExpirationTimestamp = 2_000,
            tokenCreationTimestamp = 1_000
        )

        assertEquals("token", result.token)
        assertEquals(2_000L, result.tokenExpirationTimestamp)
        assertEquals(1_000L, (result as InstallationTokenResult).tokenCreationTimestamp)
    }

    @Test
    fun testDistinguishesOfficialCreationTimestampSupport() {
        val supported = InstallationTokenResult(
            token = "android-token",
            tokenExpirationTimestamp = 2_000,
            tokenCreationTimestamp = 1_000
        )
        val unsupported = InstallationTokenResult(
            token = "ios-token",
            tokenExpirationTimestamp = 2_000,
            tokenCreationTimestamp = InstallationTokenResult.UNAVAILABLE_TOKEN_CREATION_TIMESTAMP
        )

        assertTrue(supported.hasTokenCreationTimestamp)
        assertFalse(unsupported.hasTokenCreationTimestamp)
    }

    @Test
    fun testPublicTimePropertiesSpecifyEpochMillisecondsContract() {
        val result = InstallationTokenResult(
            token = "token",
            tokenExpirationTimestamp = 1_710_003_600_000,
            tokenCreationTimestamp = 1_710_000_000_000
        )

        assertEquals(1_710_003_600_000L, result.tokenExpirationTimestampMillis)
        assertEquals(1_710_000_000_000L, result.tokenCreationTimestampMillis)
    }

    @Test
    fun FID_캐시와_리스너_등록_상태를_메모리에_보존한다() {
        val state = FidMemoryState()
        val received = mutableListOf<String>()
        val handle = state.registerFidListener(FidListener(received::add))

        state.recordFid("fid-1")
        state.recordFid("fid-1")
        state.recordFid("fid-2")
        assertEquals(listOf("fid-1", "fid-2"), received)

        state.clearFidCache()
        state.recordFid("fid-2")
        assertEquals(listOf("fid-1", "fid-2", "fid-2"), received)

        handle.unregister()
        state.recordFid("fid-3")
        assertEquals(listOf("fid-1", "fid-2", "fid-2"), received)
    }

    @Test
    fun testListenerExceptionDoesNotBlockOtherListeners() {
        val state = FidMemoryState()
        val received = mutableListOf<String>()
        state.registerFidListener(FidListener { error("listener failure") })
        state.registerFidListener(FidListener(received::add))

        state.recordFid("fid")

        assertEquals(listOf("fid"), received)
    }

    @Test
    fun testSameAppSharesInstanceAndFidState() {
        val cache = IdentityInstanceCache<Any>()
        var creationCount = 0

        val first = cache.getOrCreate("app") { Any().also { creationCount++ } }
        val second = cache.getOrCreate("app") { Any().also { creationCount++ } }
        val other = cache.getOrCreate("other") { Any().also { creationCount++ } }

        assertSame(first, second)
        assertNotSame(first, other)
        assertEquals(2, creationCount)
    }

    @OptIn(ExperimentalAtomicApi::class)
    @Test
    fun testAllConcurrentlyRegisteredListenersReceiveNotifications() = runBlocking {
        val state = FidMemoryState()
        val callbackCount = AtomicInt(0)
        val start = CompletableDeferred<Unit>()
        val listenerCount = 2_000

        val handles = (0 until listenerCount).map {
            async(Dispatchers.Default) {
                start.await()
                state.registerFidListener(FidListener { callbackCount.addAndFetch(1) })
            }
        }
        start.complete(Unit)
        handles.awaitAll()

        state.recordFid("fid")

        assertEquals(listenerCount, callbackCount.load())
    }
}
