package com.example.letsbuy.model

import com.example.letsbuy.dto.AdvertisementResponse

data class DetailAdvertisementResponse(
    val userId: Int,
    val adversiments: AdvertisementResponse,
    val isLike: Boolean,
    val likeId: Int
)
