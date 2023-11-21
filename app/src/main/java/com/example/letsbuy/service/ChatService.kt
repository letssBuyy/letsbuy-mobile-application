package com.example.letsbuy.service

import com.example.letsbuy.dto.ChatResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatService {

    @GET("/api/chats/{userId}")
    fun getChats(@Path("userId") userId: Long?): retrofit2.Call<List<ChatResponseDto>>

}