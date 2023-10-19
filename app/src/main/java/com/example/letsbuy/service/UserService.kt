package com.example.letsbuy.service

import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.dto.UserDto
import com.example.letsbuy.dto.UserDtoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.net.URI

interface UserService {

    @GET("/users")
    fun getAdversimentsByUser(@Query("sellerId") sellerId: Long, @Query("buyerId") buyerId: Long?): Call<UserAdversimentsDtoResponse>

    @POST("/users/register")
    fun createUser(@Body user: UserDto): Call<UserDtoResponse>
}