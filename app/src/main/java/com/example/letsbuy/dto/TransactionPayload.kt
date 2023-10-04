package com.example.letsbuy.dto

data class TransactionPayload(
    val userId: Long,
    val amount: Double,
    val transactionType: String
)
