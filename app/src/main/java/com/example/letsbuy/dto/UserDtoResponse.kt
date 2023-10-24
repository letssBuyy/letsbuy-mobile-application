package com.example.letsbuy.dto

data class UserDtoResponse(
    val id: Long,
    val name: String,
    val email: String,
    val cpf: String,
    val birthDate: String,
    val phoneNumber: String,
    val cep: String,
    val road: String,
    val number: Long,
    val neighborhood: String,
    val complement: String,
    val state: String,
    val city: String,
    val isActive: String,
    val profileImage: String,
    val registrationDate: String,
    val balance: Double,
    var bankAccount: BankAccount? = null,
    val accessLevel: String
)