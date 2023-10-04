package com.example.letsbuy.dto

data class AdvertisementResponse (
    val userId: Long,
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val postDate: String,
    val lastUpdate: String,
    val saleDate: String,
    val category: String,
    val quality: String,
    val color: String,
    val isActive: String,
    val contest: String,
    val images: List<ImageDtoResponse>? = null,
    val trackings: List<TrackingResponseDto>? = null,
    val paymentUserAdversiment: PaymentUserAdvertisementResponseDto? = null,
    val buyer: UserDtoResponse? = null
)