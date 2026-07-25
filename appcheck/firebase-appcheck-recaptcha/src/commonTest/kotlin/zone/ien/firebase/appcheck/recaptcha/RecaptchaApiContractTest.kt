package zone.ien.firebase.appcheck.recaptcha

import kotlin.test.Test
import kotlin.test.assertEquals

class RecaptchaApiContractTest {
    @Test
    fun 리캡차_팩토리가_공통_API에_노출된다() {
        assertEquals(
            "RecaptchaAppCheckProviderFactory",
            RecaptchaAppCheckProviderFactory::class.simpleName
        )
    }
}
