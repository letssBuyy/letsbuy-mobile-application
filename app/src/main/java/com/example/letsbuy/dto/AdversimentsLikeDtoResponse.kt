package com.example.letsbuy.dto

data class AdversimentsLikeDtoResponse (
    val id: Long,
    val userId: Long,
    val adversiments: UserLikeDto
)
