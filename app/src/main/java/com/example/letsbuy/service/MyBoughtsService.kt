package com.example.letsbuy.service

import com.example.letsbuy.dto.MyBoughtsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyBoughtsService {

    @GET("/api/adversiments/boughts/{userId}")
    fun getBoughts(@Path("userId") userId: Long): Call<List<MyBoughtsResponse>>

}