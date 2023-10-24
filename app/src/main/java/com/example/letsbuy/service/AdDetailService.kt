package com.example.letsbuy.service

import com.example.letsbuy.model.DetailAdvertisementResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AdDetailService {
    @GET("/adversiments/{id}")
    fun getDetail(@Path("id") id: Long,
                  @Query("idUser") userId: Long): Call<List<DetailAdvertisementResponse>>
}