package zone.ien.firebase.firestore

import com.google.firebase.Timestamp as AndroidTimestamp
import com.google.firebase.firestore.FieldValue as AndroidFieldValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.coroutines.Continuation

class FirestoreValueMapperAndroidTest {
    @Test
    fun Query와_DocumentReference의_기존_무인자_JVM_ABI를_보존한다() {
        Query::class.java.getDeclaredMethod("get", Continuation::class.java)
        Query::class.java.getDeclaredMethod("snapshots")
        DocumentReference::class.java.getDeclaredMethod("get", Continuation::class.java)
        DocumentReference::class.java.getDeclaredMethod("snapshots")
    }

    @Test
    fun Timestamp는_Android_Timestamp로_왕복_변환된다() {
        val timestamp = Timestamp(seconds = 1_700_000_000L, nanoseconds = 123_456_789)

        val native = timestamp.toAndroidValue()

        assertEquals(AndroidTimestamp(1_700_000_000L, 123_456_789), native)
        assertEquals(timestamp, native.toCommonValue())
    }

    @Test
    fun FieldValue_센티널은_Android_FieldValue로_변환된다() {
        assertSame(AndroidFieldValue.delete(), FieldValue.delete().toAndroidValue())
        assertSame(
            AndroidFieldValue.serverTimestamp(),
            FieldValue.serverTimestamp().toAndroidValue()
        )
        assertIs<AndroidFieldValue>(FieldValue.arrayUnion("a", 1L).toAndroidValue())
        assertIs<AndroidFieldValue>(FieldValue.arrayRemove("a").toAndroidValue())
        assertIs<AndroidFieldValue>(FieldValue.increment(1L).toAndroidValue())
        assertIs<AndroidFieldValue>(FieldValue.increment(0.5).toAndroidValue())
    }

    @Test
    fun 중첩_데이터의_Timestamp와_FieldValue를_재귀_변환한다() {
        val native = mapOf(
            "createdAt" to Timestamp(1L, 2),
            "nested" to mapOf("updatedAt" to FieldValue.serverTimestamp()),
            "history" to listOf(Timestamp(3L, 4))
        ).toAndroidData()

        assertEquals(AndroidTimestamp(1L, 2), native["createdAt"])
        assertSame(
            AndroidFieldValue.serverTimestamp(),
            (native["nested"] as Map<*, *>)["updatedAt"]
        )
        assertEquals(AndroidTimestamp(3L, 4), (native["history"] as List<*>).single())
    }
}
