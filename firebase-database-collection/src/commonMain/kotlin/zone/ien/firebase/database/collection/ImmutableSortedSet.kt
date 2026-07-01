package zone.ien.firebase.database.collection

public class ImmutableSortedSet<T> private constructor(
    private val list: List<T>,
    private val comparator: Comparator<T>
) : Iterable<T> {

    public fun contains(entry: T): Boolean = binarySearch(entry) >= 0

    public fun insert(entry: T): ImmutableSortedSet<T> {
        val index = binarySearch(entry)
        if (index >= 0) return this
        val insertionPoint = -(index + 1)
        val newList = list.toMutableList()
        newList.add(insertionPoint, entry)
        return ImmutableSortedSet(newList, comparator)
    }

    public fun remove(entry: T): ImmutableSortedSet<T> {
        val index = binarySearch(entry)
        if (index >= 0) {
            val newList = list.toMutableList()
            newList.removeAt(index)
            return ImmutableSortedSet(newList, comparator)
        }
        return this
    }

    public fun size(): Int = list.size

    public fun isEmpty(): Boolean = list.isEmpty()

    override fun iterator(): Iterator<T> = list.iterator()

    private fun binarySearch(entry: T): Int {
        return list.binarySearch(entry, comparator = comparator)
    }

    public companion object {
        public fun <T> emptySet(comparator: Comparator<T>): ImmutableSortedSet<T> {
            return ImmutableSortedSet(emptyList(), comparator)
        }
    }
}
