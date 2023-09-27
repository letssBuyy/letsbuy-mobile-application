package com.example.letsbuy.service

import com.example.letsbuy.model.Wallet
import retrofit2.Call
import retrofit2.http.GET

interface WalletService {

    @GET("/users/transaction/3")
    fun getWallet(): Call<Wallet>
}