package ru.shpzdsh.domain

import ru.shpzdsh.domain.modelsdto.MangaDto
import ru.shpzdsh.domain.modelsdto.SearchResponseDto
import ru.shpzdsh.domain.modelsdto.UpdatedMangaDto


interface MangaRepository {

    suspend fun lastUpdatedMangas(): List<UpdatedMangaDto>

    suspend fun findMangaByName(name: String): List<UpdatedMangaDto>

//    suspend fun getMangaById(id: String): MangaDto

    suspend fun getMangaChapter(mangaId: String?, chapterId: String?): List<String>

    suspend fun getMangaByCategoryId(categoryId: String, page: Int): SearchResponseDto

    suspend fun getMangaByPopularity(page: Int): SearchResponseDto

    suspend fun getMangaByName(name: String): MangaDto


}