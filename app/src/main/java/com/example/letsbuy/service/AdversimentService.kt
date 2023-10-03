package com.example.letsbuy.service

import com.example.letsbuy.dto.AdversimentDto
import com.example.letsbuy.model.AdvertisementResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AdversimentService {

    @POST("/adversiments")
    fun createAdversiment(@Body adversimentDto: AdversimentDto): Call<AdvertisementResponse>
}