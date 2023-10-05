package com.example.letsbuy.service

import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {

    @GET("/users")
    fun getAdversimentsByUser(@Query("sellerId") sellerId: Long, @Query("buyerId") buyerId: Long?): Call<UserAdversimentsDtoResponse>
}