package kr.galaxyhub.sc.crawler.application

import kr.galaxyhub.sc.news.application.NewsCreateCommand

interface Crawler {

    fun canCrawl(url: String): Boolean

    fun crawling(url: String): NewsCreateCommand
}
