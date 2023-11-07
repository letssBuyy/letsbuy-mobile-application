package com.example.letsbuy.dto

import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.AdversimentEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.QualityEnum

data class UserLikeDto(
     val id: Long,
     val title: String,
     val description: String? = null,
     val price: Double,
     val postDate: String,
     val lastUpdate: String,
     val saleDate: String? = null,
     val category: CategoryEnum,
     val quality: QualityEnum,
     val color: AdversimentColorEnum,
     val isActive: AdversimentEnum,
     val contest: AdversimentEnum,
     val images: List<ImageDtoResponse>? = null,
     val userSellerLikeDto: UserSellerLikeDto
)