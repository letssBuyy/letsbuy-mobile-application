package com.example.letsbuy.dto

import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.QualityEnum

data class AdversimentDto(
    private val userId: Long,
    private val title: String,
    private val description: String,
    private val price: Double,
    private val color: AdversimentColorEnum,
    private val category: CategoryEnum,
    private val quality: QualityEnum
)