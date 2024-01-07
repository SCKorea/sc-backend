package kr.galaxyhub.sc.crawler.domain

import java.net.URL

interface DocumentOption {
    val url: URL
    val bodyClassNames: List<String>
    val timeout: Int
}
