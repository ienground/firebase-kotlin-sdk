package zone.ien.firebase.perf

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.zone.ien.firebase.firebase.perf.FIRTrace

@OptIn(ExperimentalForeignApi::class)
public actual class Trace internal constructor(
    private val iosTrace: FIRTrace
) {
    public actual fun start() {
        iosTrace.start()
    }

    public actual fun stop() {
        iosTrace.stop()
    }

    public actual fun incrementMetric(metricName: String, incrementBy: Long) {
        iosTrace.incrementMetric(metricName, byInt = incrementBy)
    }

    public actual fun putMetric(metricName: String, value: Long) {
        iosTrace.setIntValue(value, forMetric = metricName)
    }

    public actual fun getLongMetric(metricName: String): Long {
        return iosTrace.valueForIntMetric(metricName)
    }

    public actual fun putAttribute(attribute: String, value: String) {
        iosTrace.setValue(value, forAttribute = attribute)
    }

    public actual fun getAttribute(attribute: String): String? {
        return iosTrace.valueForAttribute(attribute)
    }

    public actual fun removeAttribute(attribute: String) {
        iosTrace.removeAttribute(attribute)
    }

    @Suppress("UNCHECKED_CAST")
    public actual val attributes: Map<String, String>
        get() {
            val raw = iosTrace.attributes as? Map<Any?, *> ?: return emptyMap()
            return raw.entries.associate { it.key.toString() to it.value.toString() }
        }
}
