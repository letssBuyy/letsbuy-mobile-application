package com.example.letsbuy.model

data class Wallet (
    val balance: Double,
    val transactions: List<Transaction>
)