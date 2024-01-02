package kr.galaxyhub.sc.common.support

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.concurrent.TimeUnit
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class MockWebServerDsl {

    private var statusCode: Int? = null
    private var body: String? = null
    private var mediaType: MediaType? = null
    private var delay: Long? = null
    private var timeUnit: TimeUnit? = null

    fun statusCode(statusCode: Int) {
        this.statusCode = statusCode
    }

    fun body(body: Any) {
        this.body = objectMapper.writeValueAsString(body)
    }

    fun mediaType(mediaType: MediaType) {
        this.mediaType = mediaType
    }

    fun delay(delay: Long, timeUnit: TimeUnit) {
        this.delay = delay
        this.timeUnit = timeUnit
    }

    internal fun perform(mockWebServer: MockWebServer) {
        val response = MockResponse()
        statusCode?.also { response.setResponseCode(it) }
        body?.also {
            response.setBody(it)
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        }
        delay?.also { response.setBodyDelay(it, timeUnit!!) }
        mediaType?.also {
            response.setHeader(HttpHeaders.CONTENT_TYPE, it)
        }
        mockWebServer.enqueue(response)
    }

    companion object {
        val objectMapper = jacksonObjectMapper()
    }
}

fun MockWebServer.enqueue(dsl: MockWebServerDsl.() -> Unit = {}) {
    MockWebServerDsl().apply(dsl).perform(this)
}
