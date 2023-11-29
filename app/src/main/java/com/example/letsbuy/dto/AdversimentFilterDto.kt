package com.example.letsbuy.dto

import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.QualityEnum

data class AdversimentFilterDto(
    private val city: String?,

    private val priceMin: Double?,

    private val priceMax: Double?,

    private val quality: QualityEnum?,

    private val category: CategoryEnum?,

    private val color: AdversimentColorEnum?,

    private val filter: Int
)
