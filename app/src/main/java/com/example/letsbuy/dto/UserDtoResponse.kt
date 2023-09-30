package com.example.letsbuy.dto

data class UserDtoResponse(
    private val id: Long,
    private val name: String,
    private val email: String,
    private val cpf: String,
    private val birthDate: String,
    private val phoneNumber: String,
    private val cep: String,
    private val road: String,
    private val number: Long,
    private val neighborhood: String,
    private val complement: String,
    private val state: String,
    private val city: String,
    private val isActive: String,
    private val profileImage: String,
    private val registrationDate: String,
    private val balance: Double,
    private var bankAccount: BankAccount? = null,
    private val accessLevel: String
)