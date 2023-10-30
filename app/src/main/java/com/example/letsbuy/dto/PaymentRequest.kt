package com.example.letsbuy.dto

data class PaymentRequest(
    val isShipment: Boolean,
    val idAdvertisement: String,
    val idUser: String,
    val cardNumber: String,
    val expMonth: String,
    val expYear: String,
    val securityCode: String,
    val holderName: String
)