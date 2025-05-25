package ru.shpzdsh.data.api

import ru.shpzdsh.data.models.Chapter
import ru.shpzdsh.data.models.Manga
import ru.shpzdsh.data.models.SearchResponse

interface MangaApi {

    suspend fun lastUpdated(): List<Manga>

    suspend fun findMangasByName(name: String): List<Manga>

//    suspend fun getMangaById(id: String): Manga

    suspend fun getMangaByName(name: String): Manga

    suspend fun getMangaChapterByLink(mangaId: String?, chapterId: String?):List<String>

    suspend fun getMangaByCategoryId(categoryId: String, page: Int): SearchResponse

    suspend fun getMangaByPopularity(page: Int): SearchResponse

}