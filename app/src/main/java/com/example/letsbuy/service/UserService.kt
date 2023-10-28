package com.example.letsbuy.service

import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.dto.UserDto
import com.example.letsbuy.dto.UserDtoResponse
import com.example.letsbuy.dto.UserUpdateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.URI

interface UserService {

    @PUT("/api/users/{id}")
    fun updateUser(@Path("id") id: Long?, @Body userDto: UserUpdateDto): Call<UserDtoResponse>

    @GET("/api/users")
    fun getAdversimentsByUser(@Query("sellerId") sellerId: Long, @Query("buyerId") buyerId: Long?): Call<UserAdversimentsDtoResponse>

    @POST("/api/users/register")
    fun createUser(@Body user: UserDto): Call<UserDtoResponse>
}