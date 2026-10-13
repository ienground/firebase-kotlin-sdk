package zone.ien.firebase

import kotlin.test.Test
import kotlin.test.assertNotNull

class FirebaseTest {
    @Test
    fun testFirebaseObjectExists() {
        assertNotNull(Firebase)
    }
}
