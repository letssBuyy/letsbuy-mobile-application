package com.example.letsbuy.service

import com.example.letsbuy.dto.TrackingResponseDto
import com.example.letsbuy.model.AdvertisementResponse
import com.example.letsbuy.model.Tracking
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MyAdvertisementService {

    @GET("/adversiments/filters/3/{status}")
    fun getAdvertisements(@Path("status") status: String): Call<List<AdvertisementResponse>>

    @DELETE("/adversiments/{idAd}")
    fun deleteAd(@Path("idAd") idAd: Long): Call<Any>

    @POST("/trackings/{userId}/{idAd}")
    fun createTracking(@Path("userId") userId: Long,
                          @Path("idAd") idAd: Long,
                          @Body trackingPayload: Tracking): Call<List<TrackingResponseDto>>

}