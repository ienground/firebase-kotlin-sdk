package zone.ien.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals

class FirebaseAIApiContractTest {
    @Test
    fun 공개_AI_타입이_공통_API에_노출된다() {
        assertEquals("FirebaseAI", FirebaseAI::class.simpleName)
        assertEquals("GenerativeModel", GenerativeModel::class.simpleName)
        assertEquals("GenerateContentResponse", GenerateContentResponse::class.simpleName)
    }
}
