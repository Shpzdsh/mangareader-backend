package ru.shpzdsh.domain.modelsdto

import kotlinx.serialization.Serializable
import ru.shpzdsh.data.models.Category

@Serializable
data class CategoryDto(
    val id: String? = null,
    val name: String? = null
)

fun Category.mapToDomain() = CategoryDto(
    id = id,
    name = name
)