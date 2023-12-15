package kr.galaxyhub.sc.news.application

interface Crawler {

    fun canCrawl(url: String): Boolean

    fun crawling(url: String): NewsCreateCommand
}
