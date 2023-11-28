package com.example.letsbuy.dto

data class MessageRequest(
    val idChat: Long,
    val message: String,
    val idUser: Long
)