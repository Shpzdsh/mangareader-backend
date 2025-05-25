package ru.shpzdsh.domain.modelsdto

import kotlinx.serialization.Serializable
import ru.shpzdsh.data.models.Chapter

@Serializable
data class ChapterDto (
//    val downloadLink: String? = null,
    val id: String? = null,
    val title: String? = null,
    val date: String? = null,
    val link: String? = null,
)

fun Chapter.mapToDomain() = ChapterDto(
//    downloadLink = downloadLink,
    id = id,
    title = title,
    date = date,
    link = link
)