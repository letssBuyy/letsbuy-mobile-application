package com.example.letsbuy.model

data class Transaction(
    val id: Long,
    val amount: Double,
    val createdAt: String,
    val transactionType: String
)
