package ru.shpzdsh.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.shpzdsh.data.api.MangaApi
import ru.shpzdsh.domain.MangaRepository
import ru.shpzdsh.domain.modelsdto.MangaDto
import ru.shpzdsh.domain.modelsdto.SearchResponseDto
import ru.shpzdsh.domain.modelsdto.UpdatedMangaDto
import ru.shpzdsh.domain.modelsdto.mapLastUpdatedManga
import ru.shpzdsh.domain.modelsdto.mapToDomain
import java.io.IOException

class MangaRepositoryImpl (
    private val mangaApi: MangaApi
) : MangaRepository {

    override suspend fun lastUpdatedMangas(): List<UpdatedMangaDto> = with(Dispatchers.IO) {
        mangaApi.lastUpdated().map { it.mapLastUpdatedManga() }
    }

    override suspend fun findMangaByName(name: String): List<UpdatedMangaDto> = with(Dispatchers.IO) {
        mangaApi.findMangasByName(name).map { it.mapLastUpdatedManga() }
    }

//    override suspend fun getMangaById(id: String): MangaDto = with(Dispatchers.IO) {
//        mangaApi.getMangaById(id).mapToDomain()
//    }

    override suspend fun getMangaByName(name: String): MangaDto = with(Dispatchers.IO) {
        mangaApi.getMangaByName(name).mapToDomain()
    }

    override suspend fun getMangaChapter(
        mangaId: String?,
        chapterId: String?
    ): List<String> = with(Dispatchers.IO){
        mangaApi.getMangaChapterByLink(mangaId, chapterId)
    }

    override suspend fun getMangaByCategoryId(
        categoryId: String,
        page: Int
    ): SearchResponseDto = with(Dispatchers.IO){
        mangaApi.getMangaByCategoryId(categoryId, page).mapToDomain()
    }

    override suspend fun getMangaByPopularity(page: Int): SearchResponseDto = with(Dispatchers.IO) {
        mangaApi.getMangaByPopularity(page).mapToDomain()
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> {
        return try {
            withContext(Dispatchers.IO) {
                Result.success(block())
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.localizedMessage}"))
        }
    }

}