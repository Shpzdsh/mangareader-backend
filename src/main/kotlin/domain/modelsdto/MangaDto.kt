package ru.shpzdsh.domain.modelsdto

import kotlinx.serialization.Serializable
import ru.shpzdsh.data.models.Manga

@Serializable
data class MangaDto(
//    val id: String? = null,
    val title: String? = null,
    val anotherTitle: String? = null,
    val description: String? = null,
    val image: String? = null,
    val rating: Float? = 0f,
    val genres: String? = null,
    val status: String? = null,
    val author: String? = null,
    val chapters: List<ChapterDto> = listOf(),
    val views: String? = null,

//    val category: List<CategoryDto> = listOf(),
//    val link: String? = null,
//    val drawler: String? = null,
//    val translator: String? = null,
)

fun Manga.mapToDomain() = MangaDto(
//    id = id,
    title = title,
    anotherTitle = anotherTitle,
    description = description,
    image = image,
    rating = rating,
    genres = genres,
    status = status,
    author = author,
    chapters = chapters.map { it.mapToDomain() },
    views = views,

//    category = category.map { it.mapToDomain() },
//    link = link,
//    drawler = drawer,
//    translator = translator,
)
