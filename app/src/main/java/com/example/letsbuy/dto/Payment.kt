package com.example.letsbuy.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class Payment (
    private val id: Long? = null,
    private val amount: BigDecimal? = null,
    private val description: String? = null,
    private val currencyId: String? = null,
    private var createdAt: String? = null,
    private val externalReference: String? = null,
    private val paymentDate: String? = null,
    private val paymentMethod: String? = null,
    private val status: String? = null
)