package com.example.letsbuy.dto

data class MessageRequestDto (
    val idChat: Long,
    val message: String,
    val idUser: Long
)
