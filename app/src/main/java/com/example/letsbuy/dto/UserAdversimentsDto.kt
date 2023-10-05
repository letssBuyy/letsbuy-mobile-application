package com.example.letsbuy.dto

data class UserAdversimentsDto (
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val postDate: String,
    val lastUpdate: String,
    val saleDate: String,
    val color: String,
    val category: String,
    val quality: String,
    val images: List<ImageDtoResponse>? = null,
    val isLike: Boolean,
    val likeId: Long
)