package com.example.letsbuy.service

import com.example.letsbuy.dto.AdvertisementResponse
import retrofit2.Call
import com.example.letsbuy.dto.ChatRequest
import com.example.letsbuy.dto.ChatResponseDto
import com.example.letsbuy.dto.MapMessage
import com.example.letsbuy.dto.MessageRequest
import com.example.letsbuy.dto.ProposalRequest
import com.example.letsbuy.dto.ProposalResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatService {
    @GET("/api/chats/{userId}")
    fun getChats(@Path("userId") userId: Long?): Call<List<ChatResponseDto>>

    @GET("/api/messages/{idChat}")
    fun getMessage(@Path("idChat") idChat: Long): Call<List<MapMessage>>

    @POST("/api/chats")
    fun createChat(@Body chatRequest: ChatRequest): Call<ChatResponseDto>

    @POST("/api/messages")
    fun sendMessage(@Body messageRequest: MessageRequest): Call<Void>

    @POST("/api/messages/proposal")
    fun createProposal(@Body proposalRequest: ProposalRequest): Call<ProposalResponse>

    @PATCH("/api/chats/{messageID}")
    fun acceptProposal(@Path("messageID") messageID: Long): Call<AdvertisementResponse>

    @DELETE("/api/messages/{messageID}")
    fun deleteProposal(@Path("messageID") messageID: Long): Call<Void>
}

