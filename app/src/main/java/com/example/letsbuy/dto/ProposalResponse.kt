package com.example.letsbuy.dto

data class ProposalResponse(
    val id: Long,
    val idUser: Long,
    val amount: Double,
    val postedAt: String
)
