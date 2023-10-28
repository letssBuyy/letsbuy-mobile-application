package com.example.letsbuy.service

import com.example.letsbuy.dto.PaymentRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentService {
    @POST("/api/payment-user-advertisements")
    fun makePayment(@Body paymentRequest: PaymentRequest): Call<Void>
}
