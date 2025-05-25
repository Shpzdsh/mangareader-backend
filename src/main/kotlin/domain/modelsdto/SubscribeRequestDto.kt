package ru.shpzdsh.domain.modelsdto

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeRequestDto(
    val mangaId: String? = null,
    val lastChapter: String? = null
)
