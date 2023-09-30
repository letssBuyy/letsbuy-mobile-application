package com.example.letsbuy.model

data class TransactionPayload(
    val userId: Long,
    val amount: Double,
    val transactionType: String
)
