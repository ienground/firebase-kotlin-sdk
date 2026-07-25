package zone.ien.firebase.annotations

import kotlin.test.Test
import kotlin.test.assertEquals
import zone.ien.firebase.annotations.concurrent.Background
import zone.ien.firebase.annotations.concurrent.Blocking
import zone.ien.firebase.annotations.concurrent.Lightweight
import zone.ien.firebase.annotations.concurrent.UiThread

class FirebaseAnnotationsApiContractTest {
    @Test
    fun 공개_어노테이션이_모두_노출된다() {
        assertEquals(
            listOf("PreviewApi", "DeferredApi", "Background", "Blocking", "Lightweight", "UiThread"),
            listOf(
                PreviewApi::class,
                DeferredApi::class,
                Background::class,
                Blocking::class,
                Lightweight::class,
                UiThread::class
            ).map { it.simpleName }
        )
    }
}
