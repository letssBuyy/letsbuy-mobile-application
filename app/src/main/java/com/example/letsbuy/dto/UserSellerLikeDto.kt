package com.example.letsbuy.dto

data class UserSellerLikeDto(
     val id: Long,
     val name: String,
     val email: String,
     val cpf: String,
     val phoneNumber: String,
     val cep: String? = null,
     val road: String? = null,
     val number: Long? = null,
     val neighborhood: String? = null,
     val complement: String? = null,
     val state: String? = null,
     val city: String? = null,
     val profileImage: String? = null,
     val registrationDate: String,
     val quantityTotalAdversiment: Long? = null,
     val quantityTotalSolded: Long? = null,
     val quantityTotalActive: Long? = null
)