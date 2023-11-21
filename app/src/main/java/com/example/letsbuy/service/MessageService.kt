package com.example.letsbuy.service

import com.example.letsbuy.dto.ChatResponseDto
import com.example.letsbuy.dto.MapMessage
import com.example.letsbuy.dto.MessageRequestDto
import com.example.letsbuy.dto.MessageResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Objects

interface MessageService {

    @GET("/api/messages/{idChat}")
    fun getMessage(@Path("idChat") idChat: Long): Call<List<MapMessage>>

    @POST("/api/messages")
    fun postMessage(@Body messageDto: MessageRequestDto ) : Call<MessageResponseDto>
}