package zone.ien.firebase.appcheck.playintegrity

import kotlin.test.Test
import kotlin.test.assertEquals

class PlayIntegrityApiContractTest {
    @Test
    fun Play_Integrity_팩토리가_공통_API에_노출된다() {
        assertEquals(
            "PlayIntegrityAppCheckProviderFactory",
            PlayIntegrityAppCheckProviderFactory::class.simpleName
        )
    }
}
