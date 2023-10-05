package com.example.letsbuy.dto

import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.QualityEnum

data class AdversimentDto(
     val userId: Long,
     val title: String,
     val description: String,
     val price: Double,
     val color: AdversimentColorEnum,
     val category: CategoryEnum,
     val quality: QualityEnum
)