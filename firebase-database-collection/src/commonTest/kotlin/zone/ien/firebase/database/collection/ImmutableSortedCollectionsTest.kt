package zone.ien.firebase.database.collection

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ImmutableSortedCollectionsTest {
    private val comparator = compareBy<Int> { it }

    @Test
    fun map은_키_순서로_순회한다() {
        val map = ImmutableSortedMap.emptyMap<Int, String>(comparator)
            .insert(3, "셋")
            .insert(1, "하나")
            .insert(2, "둘")

        assertEquals(listOf(1, 2, 3), map.map { it.key })
        assertEquals("둘", map.get(2))
        assertTrue(map.containsKey(3))
    }

    @Test
    fun map_수정은_기존_인스턴스를_변경하지_않는다() {
        val original = ImmutableSortedMap.emptyMap<Int, String>(comparator).insert(1, "이전")
        val replaced = original.insert(1, "이후")
        val removed = replaced.remove(1)

        assertEquals("이전", original.get(1))
        assertEquals("이후", replaced.get(1))
        assertTrue(removed.isEmpty())
        assertSame(original, original.remove(99))
    }

    @Test
    fun set은_정렬과_중복_제거를_보장한다() {
        val empty = ImmutableSortedSet.emptySet(comparator)
        val set = empty.insert(3).insert(1).insert(2).insert(2)

        assertEquals(listOf(1, 2, 3), set.toList())
        assertEquals(3, set.size())
        assertTrue(set.contains(2))
        assertFalse(empty.contains(2))
        assertSame(set, set.remove(99))
    }
}
