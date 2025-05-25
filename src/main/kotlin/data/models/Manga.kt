package ru.shpzdsh.data.models

data class Manga (
    val id: String? = null,
    val title: String? = null,
    val image: String? = null,
    val category: List<Category> = listOf(),
    val status: String? = null,
    val genres: String? = null,
    val anotherTitle: String? = null,
    val link: String? = null,
    val author: String? = null,
    val drawer: String? = null,
    val views: String? = null,
    val translator: String? = null,
    val description: String? = null,
    val chapters: List<Chapter> = listOf(),
    val updated: String? = null,
    val rating: Float? = 0f,
)