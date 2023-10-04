package com.example.letsbuy.dto


data class UserSellerDto (
    val id: Long,
    val name: String,
    val email: String,
    val cpf: String,
    val phoneNumber: String,
    val cep: String,
    val road: String,
    val number: Long,
    val neighborhood: String,
    val complement: String,
    val state: String,
    val city: String,
    val profileImage: String,
    val registrationDate: String,
    val quantityTotalAdversiment: Long? = null,
    val quantityTotalSolded: Long? = null,
    val quantityTotalActive: Long? = null

)