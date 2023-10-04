package com.example.letsbuy.service

import com.example.letsbuy.dto.AdversimentDto
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.model.AdvertisementResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AdversimentService {

    @POST("/adversiments")
    fun createAdversiment(@Body adversimentDto: AdversimentDto): Call<AdvertisementResponse>

    @GET("/adversiments/{idAdversiment}")
    fun getAdversimentById(@Path("idAdversiment") idAdversiment: Long, @Query("idUser") idUser: Long? = null): Call<List<AllAdversimentsAndLikeDtoResponse>>
}