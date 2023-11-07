package com.example.letsbuy.dto

import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.QualityEnum

data class AdvertisementResponse (
    val userId: Long,
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val postDate: String,
    val lastUpdate: String,
    val saleDate: String,
    val category: CategoryEnum,
    val quality: QualityEnum,
    val color: AdversimentColorEnum,
    val isActive: String,
    val contest: String,
    val images: List<ImageDtoResponse>? = null,
    val trackings: List<TrackingResponseDto>? = null,
    val paymentUserAdversiment: PaymentUserAdvertisementResponseDto? = null,
    val userSellerLikeDto: UserDtoResponse? = null
)