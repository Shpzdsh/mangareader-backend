package ru.shpzdsh.domain.modelsdto

import kotlinx.serialization.Serializable

@Serializable
data class SubscribeResponseDto(
    val mangaId: String,
    val countOfNewChapters: Int
)
