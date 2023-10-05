package com.example.letsbuy.service

import com.example.letsbuy.dto.TransactionPayload
import com.example.letsbuy.model.Wallet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface WalletService {

    @GET("/users/transaction/3")
    fun getWallet(): Call<Wallet>

    @PATCH("/users/transaction")
    fun createTransaction(@Body transactionPayload: TransactionPayload): Call<TransactionPayload>

}