package com.example.letsbuy.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Rest {
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}