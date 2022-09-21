package com.example.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText

fun Application.configureStatusPages() {
  install(StatusPages) {
    status(HttpStatusCode.NotFound) { call, status ->
      call.respondText(text = "404: Page Not Found", status = status)
    }
  }
}
