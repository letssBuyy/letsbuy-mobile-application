package com.example.letsbuy.service

import com.example.letsbuy.dto.TransactionPayload
import com.example.letsbuy.model.Wallet
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface WalletService {

    @GET("/api/users/transaction/{idUser}")
    fun getWallet(@Path("idUser") idUser: Long): Call<Wallet>

    @PATCH("/api/users/transaction")
    fun createTransaction(@Body transactionPayload: TransactionPayload): Call<TransactionPayload>

}