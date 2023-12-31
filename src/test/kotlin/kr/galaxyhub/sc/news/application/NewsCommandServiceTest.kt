package kr.galaxyhub.sc.news.application

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.MemoryNewsRepository
import kr.galaxyhub.sc.news.domain.NewsInformation
import kr.galaxyhub.sc.news.domain.NewsType
import kr.galaxyhub.sc.news.fixture.ContentFixture
import kr.galaxyhub.sc.news.fixture.NewsFixture

class NewsCommandServiceTest(
    private val newsRepository: MemoryNewsRepository = MemoryNewsRepository(),
    private val newsCommandService: NewsCommandService = NewsCommandService(newsRepository),
) : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    describe("create") {
        context("originId에 대해 저장된 뉴스가 없으면") {
            it("새로운 뉴스가 생성된다.") {
                val originId = 1L
                val newsId = withContext(Dispatchers.IO) {
                    newsCommandService.create(newsCreateCommand(originId))
                }

                val news = newsRepository.findById(newsId)!!
                news shouldNotBe null
                news.supportLanguages shouldContainExactly setOf(Language.KOREAN)
            }
        }

        context("originId에 대해 저장된 뉴스가 있으면") {
            val originId = 1L
            val existsCommand = newsCreateCommand(originId)
            withContext(Dispatchers.IO) {
                newsCommandService.create(existsCommand)
            }

            it("기존 뉴스에 새로운 언어에 대한 컨텐츠가 추가된다.") {
                val command = existsCommand.copy(language = Language.ENGLISH)
                val newsId = withContext(Dispatchers.IO) {
                    newsCommandService.create(command)
                }

                val news = newsRepository.findById(newsId)!!
                news.supportLanguages shouldContainExactly setOf(Language.KOREAN, Language.ENGLISH)
                newsRepository.findAll() shouldHaveSize 1
            }
        }
    }

    describe("updateContent") {
        val news = NewsFixture.create()
        val content = ContentFixture.create(
            newsId = news.id,
            language = Language.ENGLISH,
            newsInformation = NewsInformation("old", "old"),
            content = "old",
        )
        news.addContent(content)
        newsRepository.save(news)

        context("Command의 newsInformation이 null이면") {
            val command = NewsUpdateCommand(
                language = Language.ENGLISH,
                newsInformation = null,
                content = "update"
            )
            newsCommandService.updateContent(news.id, command)

            it("newsInformation은 수정되지 않는다.") {
                content.newsInformation shouldBe NewsInformation("old", "old")
            }

            it("content는 수정된다.") {
                content.content shouldBe "update"
            }
        }

        context("Command의 content가 null이면") {
            val command = NewsUpdateCommand(
                language = Language.ENGLISH,
                newsInformation = NewsInformation("update", "update"),
                content = null
            )
            newsCommandService.updateContent(news.id, command)

            it("content는 수정되지 않는다.") {
                content.content shouldBe "old"
            }

            it("newsInformation은 수정된다.") {
                content.newsInformation shouldBe NewsInformation("update", "update")
            }
        }
    }
})

private fun newsCreateCommand(originId: Long) = NewsCreateCommand(
    newsType = NewsType.NEWS,
    title = "제목",
    excerpt = "줄거리",
    publishedAt = ZonedDateTime.of(LocalDateTime.parse("2023-12-04T03:53:33"), ZoneId.systemDefault()),
    originId = originId,
    originUrl = "https://sc.galaxyhub.kr/",
    language = Language.KOREAN,
    content = "내용"
)
