package kr.galaxyhub.sc.crawler.domain

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 크롤링한 뉴스의 제목, 소제목, 요약을 나타냅니다.
 * @property title: 제목.
 * @property subtitle: 소제목.
 * @property summary: 요약에 대한 HTML 형식의 문자열 리스트. ex) &lt;p&gt;간단한 요약을 나타냅니다.&lt;/p&gt;
 * @author seokjin8678
 */
data class Introduction(
    val title: String,
    val subtitle: String,
    @JsonProperty("contents")
    val summary: List<String>,
)
