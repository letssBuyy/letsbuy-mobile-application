package com.example.letsbuy.service

import android.telecom.Call
import com.example.letsbuy.dto.ChatRequest
import com.example.letsbuy.dto.ChatResponseDto
import com.example.letsbuy.dto.MapMessage
import com.example.letsbuy.dto.MessageRequest
import com.example.letsbuy.dto.ProposalRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatService {
    @GET("/api/chats/{userId}")
    fun getChats(@Path("userId") userId: Long?): retrofit2.Call<List<ChatResponseDto>>

    @GET("/api/messages/{idChat}")
    fun getMessage(@Path("idChat") idChat: Long): retrofit2.Call<List<MapMessage>>

    @POST("/api/chats")
    fun createChat(@Body chatRequest: ChatRequest): retrofit2.Call<ChatResponseDto>

    @POST("/api/messages")
    fun sendMessage(@Body messageRequest: MessageRequest): retrofit2.Call<Void>

    @POST("/messages/proposal")
    fun createProposal(@Body proposalRequest: ProposalRequest)

    @PATCH("/api/chats/{messageID}")
    fun acceptProposal(@Path("messageID") messageID: String)

    @DELETE("/api/messages/{messageID}")
    fun deleteProposal(@Path("messageID") messageID: String)
}

