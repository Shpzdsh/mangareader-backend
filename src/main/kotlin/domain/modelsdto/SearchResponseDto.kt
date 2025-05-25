package ru.shpzdsh.domain.modelsdto

import kotlinx.serialization.Serializable
import ru.shpzdsh.data.models.SearchResponse

@Serializable
data class SearchResponseDto(
    val mangas: List<UpdatedMangaDto>,
    val pagesMax: Int
)

fun SearchResponse.mapToDomain() = SearchResponseDto(
    mangas = mangas.map { it.mapLastUpdatedManga() },
    pagesMax = pagesMax
)
