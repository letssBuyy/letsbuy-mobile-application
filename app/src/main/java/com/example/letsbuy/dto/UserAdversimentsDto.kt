package com.example.letsbuy.dto

import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.QualityEnum

data class UserAdversimentsDto (
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val postDate: String,
    val lastUpdate: String,
    val saleDate: String,
    val color: AdversimentColorEnum,
    val category: CategoryEnum,
    val quality: QualityEnum,
    val images: List<ImageDtoResponse>? = null,
    val isLike: Boolean,
    val likeId: Long
)