package com.example.letsbuy.api

import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Rest {

    private var okHttpClientBuilder: Builder = Builder()
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("https://letsbuy.sytes.net/")
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}