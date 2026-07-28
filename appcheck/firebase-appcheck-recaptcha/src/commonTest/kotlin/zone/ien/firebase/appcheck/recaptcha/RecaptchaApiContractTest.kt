package zone.ien.firebase.appcheck.recaptcha

import kotlin.test.Test
import kotlin.test.assertEquals

class RecaptchaApiContractTest {
    @Test
    fun testRecaptchaFactoryExposedInCommonApi() {
        assertEquals(
            "RecaptchaAppCheckProviderFactory",
            RecaptchaAppCheckProviderFactory::class.simpleName
        )
    }
}
