package zone.ien.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseAIApiContractTest {
    @Test
    fun testPublicAiTypesExposedInCommonApi() {
        assertEquals("FirebaseAI", FirebaseAI::class.simpleName)
        assertEquals("GenerativeModel", GenerativeModel::class.simpleName)
        assertEquals("GenerateContentResponse", GenerateContentResponse::class.simpleName)
    }
}
