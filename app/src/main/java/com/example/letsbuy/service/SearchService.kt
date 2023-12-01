package com.example.letsbuy.service

import com.example.letsbuy.dto.AdversimentFilterDto
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("/api/searches/android")
    fun searchHome(@Query("idUser") idUser: Long?, @Query("title") title: String?): Call<List<AllAdversimentsAndLikeDtoResponse>>

    @POST("/api/searches/filter/android")
    fun search(@Query("idUser") idUser: Long?, @Query("title") title: String?, @Body filter: AdversimentFilterDto): Call<List<AllAdversimentsAndLikeDtoResponse>>
}