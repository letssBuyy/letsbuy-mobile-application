package com.example.letsbuy.service

import com.example.letsbuy.model.AdvertisementResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyAdvertisementService {

    @GET("/adversiments/filters/3/{status}")
    fun getAdvertisements(@Path("status") status: String): Call<List<AdvertisementResponse>>

}