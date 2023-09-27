package com.example.letsbuy.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Rest {
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("https://letsbuy-api-dev.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}