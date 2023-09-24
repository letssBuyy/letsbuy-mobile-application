package com.example.letsbuy.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Rest {
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("https://61c71fed90318500175472ff.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}