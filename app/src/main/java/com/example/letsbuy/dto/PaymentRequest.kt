package com.example.letsbuy.dto

data class PaymentRequest(
    val isShipment: Boolean,
    val idAdvertisement: Long,
    val idUser: Long,
    val cardNumber: String,
    val expMonth: String,
    val expYear: String,
    val securityCode: String,
    val holderName: String
)