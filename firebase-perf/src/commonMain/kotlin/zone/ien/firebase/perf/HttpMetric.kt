package zone.ien.firebase.perf

public expect class HttpMetric(url: String, httpMethod: String) {
    public fun start()
    public fun stop()
    public fun setRequestPayloadBytes(bytes: Long)
    public fun setResponsePayloadBytes(bytes: Long)
    public fun setHttpResponseCode(code: Int)
    public fun setResponseContentType(contentType: String?)
    public fun putAttribute(attribute: String, value: String)
    public fun getAttribute(attribute: String): String?
    public fun removeAttribute(attribute: String)
    public val attributes: Map<String, String>
}
