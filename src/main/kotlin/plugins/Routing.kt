package ru.shpzdsh.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import ru.shpzdsh.domain.MangaRepository

fun Application.configureRouting(mangaRepository: MangaRepository) {

    routing {
        get("/manga") {
            runCatching { mangaRepository.lastUpdatedMangas() }
                .fold(
                    onSuccess = { call.respond(HttpStatusCode.OK, it) },
                    onFailure = { call.respond(HttpStatusCode.InternalServerError) }
                )
        }

        get("/search") {
            val name = call.request.queryParameters["name"].orEmpty()
            runCatching { call.respond(mangaRepository.findMangaByName(name)) }
                .onSuccess { call.respond(it) }
        }

        get("/manga/{name}") {
            val name = call.parameters["name"].orEmpty()
            runCatching { mangaRepository.getMangaByName(name) }
                .fold(
                    onSuccess = { call.respond(HttpStatusCode.OK, message = it) },
                    onFailure = { call.respond(HttpStatusCode.InternalServerError) }
                )

        }

        get("/manga/{mangaId}/chapterId") {
            val mangaId = call.parameters["mangaId"].orEmpty()
            val chapterId = call.parameters["chapterId"].orEmpty()
            runCatching { call.respond(mangaRepository.getMangaChapter(mangaId, chapterId)) }
                .onSuccess { call.respond(it) }
        }

        get("/mangas/{category}/{page}") {
            val category = call.parameters["category"].orEmpty()
            val page = call.parameters["page"]?.toInt() ?: 1
            runCatching { call.respond(mangaRepository.getMangaByCategoryId(category, page)) }
                .onSuccess { call.respond(it) }
        }

        get("/mangas/{page}") {
            val page = call.parameters["page"]?.toInt() ?: 1
            runCatching { call.respond(mangaRepository.getMangaByPopularity(page)) }
                .onSuccess { call.respond(it) }
        }

    }
}