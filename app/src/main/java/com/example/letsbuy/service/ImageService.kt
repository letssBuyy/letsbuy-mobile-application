package com.example.letsbuy.service

import com.example.letsbuy.dto.AdvertisementResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ImageService {

    @Multipart
    @POST("/api/images/adversiment/{id}")
    fun uploadImages(
        @Path("id") id: Long,
        @Part img1: MultipartBody.Part,
        @Part img2: MultipartBody.Part,
        @Part img3: MultipartBody.Part,
        @Part img4: MultipartBody.Part
    ): Call<AdvertisementResponse>
}