package com.example.letsbuy.dto

data class ChatResponseDto (
    var id: Long,
    var lastMessageAt: String,
    var seller: UserDtoResponse,
    var buyer: UserDtoResponse,
    var adversiment : AdversimentDtoResponse
)