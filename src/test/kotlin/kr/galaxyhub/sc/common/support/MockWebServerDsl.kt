package kr.galaxyhub.sc.common.support

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class MockWebServerDsl {

    private var statusCode: Int? = null
    private var body: String? = null
    private var mediaType: String? = null

    fun statusCode(statusCode: Int) {
        this.statusCode = statusCode
    }

    fun body(body: String) {
        this.body = body
    }

    fun mediaType(mediaType: MediaType) {
        this.mediaType = mediaType.toString()
    }

    internal fun perform(mockWebServer: MockWebServer) {
        val response = MockResponse()
        statusCode?.also { response.setResponseCode(it) }
        body?.also { response.setBody(it) }
        if (mediaType != null) {
            response.addHeader(HttpHeaders.CONTENT_TYPE, mediaType!!)
        } else {
            response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        }
        mockWebServer.enqueue(response)
    }
}

fun MockWebServer.enqueue(dsl: MockWebServerDsl.() -> Unit = {}) {
    MockWebServerDsl().apply(dsl).perform(this)
}
