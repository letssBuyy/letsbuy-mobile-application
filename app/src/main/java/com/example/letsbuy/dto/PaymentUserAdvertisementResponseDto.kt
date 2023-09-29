package com.example.letsbuy.dto

import java.time.LocalDateTime

data class PaymentUserAdvertisementResponseDto (
    private val user: UserDtoResponse? = null,
    private val isShipment: Boolean? = null,
    private val payment: Payment? = null,
    private val receivableDate: String? = null
)