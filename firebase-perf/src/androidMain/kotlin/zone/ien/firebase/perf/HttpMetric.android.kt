package zone.ien.firebase.perf

public actual class HttpMetric {
    private val androidHttpMetric: com.google.firebase.perf.metrics.HttpMetric

    public actual constructor(url: String, httpMethod: String) {
        this.androidHttpMetric = com.google.firebase.perf.FirebasePerformance.getInstance().newHttpMetric(url, httpMethod)
    }

    internal constructor(androidHttpMetric: com.google.firebase.perf.metrics.HttpMetric) {
        this.androidHttpMetric = androidHttpMetric
    }

    public actual fun start() {
        androidHttpMetric.start()
    }

    public actual fun stop() {
        androidHttpMetric.stop()
    }

    public actual fun setRequestPayloadBytes(bytes: Long) {
        androidHttpMetric.setRequestPayloadSize(bytes)
    }

    public actual fun setResponsePayloadBytes(bytes: Long) {
        androidHttpMetric.setResponsePayloadSize(bytes)
    }

    public actual fun setHttpResponseCode(code: Int) {
        androidHttpMetric.setHttpResponseCode(code)
    }

    public actual fun setResponseContentType(contentType: String?) {
        androidHttpMetric.setResponseContentType(contentType)
    }

    public actual fun putAttribute(attribute: String, value: String) {
        androidHttpMetric.putAttribute(attribute, value)
    }

    public actual fun getAttribute(attribute: String): String? {
        return androidHttpMetric.getAttribute(attribute)
    }

    public actual fun removeAttribute(attribute: String) {
        androidHttpMetric.removeAttribute(attribute)
    }

    public actual val attributes: Map<String, String>
        get() = androidHttpMetric.attributes
}
