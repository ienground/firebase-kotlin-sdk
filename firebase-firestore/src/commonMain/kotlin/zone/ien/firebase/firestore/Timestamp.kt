package zone.ien.firebase.firestore

data class Timestamp(
    val seconds: Long,
    val nanoseconds: Int
) : Comparable<Timestamp> {
    init {
        require(seconds in MIN_SECONDS..MAX_SECONDS) {
            "Timestamp seconds must be between $MIN_SECONDS and $MAX_SECONDS."
        }
        require(nanoseconds in 0..999_999_999) {
            "Timestamp nanoseconds must be between 0 and 999,999,999."
        }
    }

    override fun compareTo(other: Timestamp): Int {
        val secondsComparison = seconds.compareTo(other.seconds)
        return if (secondsComparison != 0) {
            secondsComparison
        } else {
            nanoseconds.compareTo(other.nanoseconds)
        }
    }

    companion object {
        private const val MIN_SECONDS = -62_135_596_800L
        const val MAX_SECONDS = 253_402_300_799L

        fun now(): Timestamp {
            val currentMillis = epochMillis()
            val seconds = currentMillis / 1000
            val nanos = ((currentMillis % 1000) * 1_000_000).toInt()
            return Timestamp(seconds, if (nanos < 0) nanos + 1_000_000_000 else nanos)
        }

        fun fromMilliseconds(milliseconds: Double): Timestamp = fromMilliseconds(milliseconds.toLong())

        fun fromMilliseconds(milliseconds: Long): Timestamp {
            val seconds = milliseconds / 1000
            val nanos = ((milliseconds % 1000) * 1_000_000).toInt()
            val adjustedNanos = if (nanos < 0) nanos + 1_000_000_000 else nanos
            val adjustedSeconds = if (nanos < 0) seconds - 1 else seconds
            return Timestamp(adjustedSeconds, adjustedNanos)
        }
    }
}

internal expect fun epochMillis(): Long
