package com.example.letsbuy.model

import com.example.letsbuy.dto.AdTrackingPayload

data class Tracking(
    val status: String,
    val adversiment: AdTrackingPayload
)
