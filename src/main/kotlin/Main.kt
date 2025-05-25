package ru.shpzdsh

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ru.shpzdsh.data.api.MangaApiImpl
import ru.shpzdsh.data.repository.MangaRepositoryImpl
import ru.shpzdsh.plugins.configureRouting
import ru.shpzdsh.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }
        .start(wait = true)
}

fun Application.module() {
    val mangaApi = MangaApiImpl()
    val mangaRepository = MangaRepositoryImpl(mangaApi)
    configureSerialization()
    configureRouting(mangaRepository)
}