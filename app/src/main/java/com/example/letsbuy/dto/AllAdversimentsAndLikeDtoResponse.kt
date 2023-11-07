package com.example.letsbuy.dto

data class AllAdversimentsAndLikeDtoResponse(
    val userId: Long?,
    val adversiments: UserLikeDto,
    val isLike: Boolean,
    val likeId: Long
)