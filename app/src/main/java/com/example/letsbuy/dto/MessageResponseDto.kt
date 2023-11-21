package com.example.letsbuy.dto

data class MessageResponseDto (
    val id: Long,
    val message: String,
    val idUser: Long,
    val isProposal: Boolean,
    val amount: Double,
    val postedAt: String
)
