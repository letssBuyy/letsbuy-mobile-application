package com.example.letsbuy.service

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeService {
    @POST("/api/adversiments/like/{userId}/{advertisementId}")
    fun like(@Path("advertisementId") advertisementId: Long,
             @Path("userId") userId: Long): Call<Void>

    @DELETE("/api/adversiments/deslike/{advertisementId}")
    fun unLike(@Path("advertisementId") advertisementId: Long): Call<Void>
}