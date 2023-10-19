package com.example.letsbuy.service

import com.example.letsbuy.dto.AuthenticationRequestDto
import com.example.letsbuy.dto.TokenDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("/auth")
    fun authentication(@Body dto: AuthenticationRequestDto): Call<TokenDto>
}