package ru.shpzdsh.data.models

data class SearchResponse(
    val mangas: List<Manga>,
    val pagesMax: Int
)
