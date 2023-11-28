package com.example.letsbuy.service

import com.example.letsbuy.dto.AdversimentDto
import com.example.letsbuy.dto.AdversimentsLikeDtoResponse
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
    @GET("/api/adversiments/like/{idUser}")
    fun getAdversimentLike(@Path("idUser") idUser: Long?): Call<List<AdversimentsLikeDtoResponse>>

    @GET("/api/adversiments/androidAdversiment/{idUser}")
    fun retrieveAdversiment(@Path("idUser") userId: Long?): Call<List<AllAdversimentsAndLikeDtoResponse>>

    @POST("/api/adversiments")
    fun createAdversiment(@Body adversimentDto: AdversimentDto): Call<AdvertisementResponse>

    @GET("/api/adversiments/{idAdversiment}")
    fun getAdversimentById(@Path("idAdversiment") idAdversiment: Long, @Query("idUser") idUser: Long? = null): Call<List<AllAdversimentsAndLikeDtoResponse>>

    @PUT("/api/adversiments/{id}")
    fun updateAdversiment(@Path("id") id: Long, @Body adversimentDto: AdversimentDto): Call<AdvertisementResponse>
}