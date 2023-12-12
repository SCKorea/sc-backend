package kr.galaxyhub.sc.api.support

import org.springframework.http.HttpMethod
import org.springframework.restdocs.generate.RestDocumentationGenerator
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.head
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.options
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request


/**
 * https://github.com/Ninja-Squad/spring-rest-docs-kotlin
 */

/**
 * Create a [ResultActionsDsl] for a GET request. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.get] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docGet(
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return get(urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}

/**
 * Create a [ResultActionsDsl] for a POST request. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.post] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docPost(
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return post(urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}

/**
 * Create a [ResultActionsDsl] for a PUT request. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.put] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docPut(
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return put(urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}

/**
 * Create a [ResultActionsDsl] for a DELETE request. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.delete] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docDelete(
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return delete(urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}

/**
 * Create a [ResultActionsDsl] for a HEAD request. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.head] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docHead(
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return head(urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}


/**
 * Create a [ResultActionsDsl] for an OPTIONS request. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.options] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docOptions(
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return options(urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}

/**
 * Create a [ResultActionsDsl] for a PATCH request. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.patch] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docPatch(
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return patch(urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}

/**
 * Create a [ResultActionsDsl] for a multipart request. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.multipart] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docMultipart(
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return multipart(urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}

/**
 * Create a [ResultActionsDsl] for a request with the given HTTP method. The URL template
 * will be captured and made available for documentation.
 * Use this method instead of [MockMvc.multipart] in order to be able to document the URL template and path parameters
 * @param urlTemplate a URL template; the resulting URL will be encoded
 * @param urlVariables zero or more URL variables
 * @return the DSL allowing to apply expectations, handlers, and to document the request
 */
fun MockMvc.docRequest(
    method: HttpMethod,
    urlTemplate: String,
    vararg urlVariables: Any?,
    dsl: MockHttpServletRequestDsl.() -> Unit = {}
): ResultActionsDsl {
    return request(method, urlTemplate, *urlVariables) {
        requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, urlTemplate);
        dsl()
    }
}

fun MockHttpServletRequestDsl.param(pair: Pair<String, Any>) {
    this.param(pair.first, pair.second.toString())
}

fun ResultActionsDsl.andDocument(
    identifier: String,
    dsl: RestDocDsl.() -> Unit = {}
): ResultActionsDsl {
    return RestDocDsl().apply(dsl).perform(identifier, this)
}
