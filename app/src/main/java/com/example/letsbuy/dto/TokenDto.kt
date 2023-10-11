package com.example.letsbuy.dto

data class TokenDto (
    var user: UserDtoResponse,
    var token: String,
    var tipo: String
)