package com.example

import com.example.di.koinModule
import com.example.models.ApiResponse
import com.example.plugins.configureRouting
import com.example.repository.HeroRepository
import com.example.repository.NEXT_PAGE_KEY
import com.example.repository.PREVIOUS_PAGE_KEY
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.testApplication
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.check.checkKoinModules
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

  private val heroRepository: HeroRepository by inject(HeroRepository::class.java)

  @Test
  fun `access root endpoint, assert correct information`() = testApplication {
    config()
    client.get("/").apply {
      assertEquals(HttpStatusCode.OK, status)
      assertEquals("\"Welcome\"", actual = bodyAsText())
    }
  }

  @ExperimentalSerializationApi
  @Test
  fun `access all heroes endpoint, query all pages, assert correct information`() {
    withTestApplication(moduleFunction = Application::module) {
      val pages = 1..5
      val heroes = listOf(
        heroRepository.page1,
        heroRepository.page2,
        heroRepository.page3,
        heroRepository.page4,
        heroRepository.page5
      )
      pages.forEach { page ->
        handleRequest(HttpMethod.Get, "/boruto/heroes?page=$page").apply {
          assertEquals(
            expected = HttpStatusCode.OK,
            actual = response.status()
          )
          val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
          val expected = ApiResponse(
            success = true,
            message = "ok",
            prevPage = calculatePage(page = page)["prevPage"],
            nextPage = calculatePage(page = page)["nextPage"],
            heroes = heroes[page - 1],
          )
          assertEquals(
            expected = expected,
            actual = actual
          )
        }
      }
    }
  }

  fun config() {
    testApplication {
      application {
        configureRouting()
      }
    }
  }

  fun configKoin() {
    testApplication {
      application {
        checkKoinModules(listOf(koinModule))
        configureRouting()
      }
    }
  }

  private fun calculatePage(page: Int): Map<String, Int?> {
    var prevPage: Int? = page
    var nextPage: Int? = page
    if (page in 1..4) {
      nextPage = nextPage?.plus(1)
    }
    if (page in 2..5) {
      prevPage = prevPage?.minus(1)
    }
    if (page == 1) {
      prevPage = null
    }
    if (page == 5) {
      nextPage = null
    }
    return mapOf(PREVIOUS_PAGE_KEY to prevPage, NEXT_PAGE_KEY to nextPage)
  }

}
