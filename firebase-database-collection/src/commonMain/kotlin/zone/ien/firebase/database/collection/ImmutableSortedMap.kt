package zone.ien.firebase.database.collection

public class ImmutableSortedMap<K, V> private constructor(
    private val entries: List<Pair<K, V>>,
    private val comparator: Comparator<K>
) : Iterable<Map.Entry<K, V>> {

    public fun get(key: K): V? {
        val index = binarySearch(key)
        return if (index >= 0) entries[index].second else null
    }

    public fun insert(key: K, value: V): ImmutableSortedMap<K, V> {
        val index = binarySearch(key)
        val newEntries = entries.toMutableList()
        if (index >= 0) {
            newEntries[index] = Pair(key, value)
        } else {
            val insertionPoint = -(index + 1)
            newEntries.add(insertionPoint, Pair(key, value))
        }
        return ImmutableSortedMap(newEntries, comparator)
    }

    public fun remove(key: K): ImmutableSortedMap<K, V> {
        val index = binarySearch(key)
        if (index >= 0) {
            val newEntries = entries.toMutableList()
            newEntries.removeAt(index)
            return ImmutableSortedMap(newEntries, comparator)
        }
        return this
    }

    public fun containsKey(key: K): Boolean = binarySearch(key) >= 0

    public fun size(): Int = entries.size

    public fun isEmpty(): Boolean = entries.isEmpty()

    override fun iterator(): Iterator<Map.Entry<K, V>> {
        return entries.asSequence().map { entry ->
            object : Map.Entry<K, V> {
                override val key: K = entry.first
                override val value: V = entry.second
            }
        }.iterator()
    }

    private fun binarySearch(key: K): Int {
        return entries.binarySearch { entry ->
            comparator.compare(entry.first, key)
        }
    }

    public companion object {
        public fun <K, V> emptyMap(comparator: Comparator<K>): ImmutableSortedMap<K, V> {
            return ImmutableSortedMap(emptyList(), comparator)
        }
    }
}
