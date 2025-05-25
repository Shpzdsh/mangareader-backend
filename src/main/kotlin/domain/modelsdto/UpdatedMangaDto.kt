package ru.shpzdsh.domain.modelsdto

import kotlinx.serialization.Serializable
import ru.shpzdsh.data.models.Manga

@Serializable
data class UpdatedMangaDto(
    val title: String? = null,
    val imageUrl: String? = null,
    val link: String? = null,
    val description: String? = null,
    val updated: String? = null,
)

fun Manga.mapLastUpdatedManga(): UpdatedMangaDto = UpdatedMangaDto(
    title = title.orEmpty(),
    imageUrl = image.orEmpty(),
    link = link.orEmpty(),
    description = description.orEmpty(),
    updated = updated.orEmpty(),
)
