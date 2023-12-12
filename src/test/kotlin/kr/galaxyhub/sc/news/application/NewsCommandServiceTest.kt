package kr.galaxyhub.sc.news.application

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.galaxyhub.sc.news.domain.Language
import kr.galaxyhub.sc.news.domain.MemoryNewsRepository
import kr.galaxyhub.sc.news.domain.NewsType

class NewsCommandServiceTest(
    private val newsRepository: MemoryNewsRepository = MemoryNewsRepository(),
    private val newsCommandService: NewsCommandService = NewsCommandService(newsRepository),
) : DescribeSpec({

    beforeContainer { newsRepository.clear() }

    describe("create") {
        context("originId에 대해 저장된 뉴스가 없으면") {
            it("새로운 뉴스가 생성된다.") {
                // given
                val command = newsCreateCommand()

                // when
                val id = withContext(Dispatchers.IO) {
                    newsCommandService.create(command)
                }

                // then
                val news = newsRepository.findById(id)!!
                news shouldNotBe null
                news.supportLanguages shouldContainExactly setOf(Language.KOREAN)
            }
        }

        context("originId에 대해 저장된 뉴스가 있으면") {
            it("기존 뉴스에 새로운 언어에 대한 컨텐츠가 추가된다.") {
                // given
                val existsCommand = newsCreateCommand()
                withContext(Dispatchers.IO) {
                    newsCommandService.create(existsCommand)
                }

                // when
                val command = existsCommand.copy(language = Language.ENGLISH)
                val id = withContext(Dispatchers.IO) {
                    newsCommandService.create(command)
                }

                // then
                val news = newsRepository.findById(id)!!
                news.supportLanguages shouldContainExactly setOf(Language.KOREAN, Language.ENGLISH)
                newsRepository.findAll() shouldHaveSize 1
            }
        }
    }
})

private fun newsCreateCommand() = NewsCreateCommand(
    newsType = NewsType.NEWS,
    title = "제목",
    excerpt = "줄거리",
    publishedAt = ZonedDateTime.of(LocalDateTime.parse("2023-12-04T03:53:33"), ZoneId.systemDefault()),
    originId = 1,
    originUrl = "https://sc.galaxyhub.kr/",
    language = Language.KOREAN,
    content = "내용"
)
