package com.example.letsbuy.service

import com.example.letsbuy.dto.AdversimentDto
import com.example.letsbuy.dto.AdvertisementResponse
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Optional

interface AdversimentService {
    @GET("/adversiments/androidAdversiment/{idUser}")
    fun retrieveAdversiment(@Path("idUser") userId: Long?): Call<List<AllAdversimentsAndLikeDtoResponse>>

    @POST("/adversiments")
    fun createAdversiment(@Body adversimentDto: AdversimentDto): Call<AdvertisementResponse>

    @GET("/adversiments/{idAdversiment}")
    fun getAdversimentById(@Path("idAdversiment") idAdversiment: Long, @Query("idUser") idUser: Long? = null): Call<List<AllAdversimentsAndLikeDtoResponse>>

    @PUT("/adversiments/{id}")
    fun updateAdversiment(@Path("id") id: Long, @Body adversimentDto: AdversimentDto): Call<AdvertisementResponse>
}