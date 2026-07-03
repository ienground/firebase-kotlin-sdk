package zone.ien.firebase.perf

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import swiftPMImport.zone.ien.firebase.firebase.perf.FIRHTTPMetric
import swiftPMImport.zone.ien.firebase.firebase.perf.FIRHTTPMethod

@OptIn(ExperimentalForeignApi::class)
public actual class HttpMetric {
    private val iosHttpMetric: FIRHTTPMetric

    public actual constructor(url: String, httpMethod: String) {
        val nsUrl = NSURL.URLWithString(url) ?: throw IllegalArgumentException("Invalid URL: $url")
        val method: FIRHTTPMethod = when (httpMethod.uppercase()) {
            "GET" -> FIRHTTPMethod.FIRHTTPMethodGET
            "POST" -> FIRHTTPMethod.FIRHTTPMethodPOST
            "PUT" -> FIRHTTPMethod.FIRHTTPMethodPUT
            "DELETE" -> FIRHTTPMethod.FIRHTTPMethodDELETE
            "HEAD" -> FIRHTTPMethod.FIRHTTPMethodHEAD
            "PATCH" -> FIRHTTPMethod.FIRHTTPMethodPATCH
            "OPTIONS" -> FIRHTTPMethod.FIRHTTPMethodOPTIONS
            "TRACE" -> FIRHTTPMethod.FIRHTTPMethodTRACE
            "CONNECT" -> FIRHTTPMethod.FIRHTTPMethodCONNECT
            else -> FIRHTTPMethod.FIRHTTPMethodGET
        }
        this.iosHttpMetric = FIRHTTPMetric(uRL = nsUrl, HTTPMethod = method)
    }

    public actual fun start() {
        iosHttpMetric.start()
    }

    public actual fun stop() {
        iosHttpMetric.stop()
    }

    public actual fun setRequestPayloadBytes(bytes: Long) {
        iosHttpMetric.requestPayloadSize = bytes
    }
    public actual fun setResponsePayloadBytes(bytes: Long) {
        iosHttpMetric.responsePayloadSize = bytes
    }
    public actual fun setHttpResponseCode(code: Int) {
        iosHttpMetric.responseCode = code.toLong()
    }
    public actual fun setResponseContentType(contentType: String?) {
        iosHttpMetric.responseContentType = contentType
    }

    public actual fun putAttribute(attribute: String, value: String) {
        iosHttpMetric.setValue(value, forAttribute = attribute)
    }

    public actual fun getAttribute(attribute: String): String? {
        return iosHttpMetric.valueForAttribute(attribute)
    }

    public actual fun removeAttribute(attribute: String) {
        iosHttpMetric.removeAttribute(attribute)
    }

    @Suppress("UNCHECKED_CAST")
    public actual val attributes: Map<String, String>
        get() {
            val raw = iosHttpMetric.attributes as? Map<Any?, *> ?: return emptyMap()
            return raw.entries.associate { it.key.toString() to it.value.toString() }
        }
}
