package com.example.letsbuy.dto

import java.time.LocalDate

data class UserDto (
    var name: String,
    var email: String,
    var cpf: String,
    var password: String,
    var birthDate: LocalDate,
    var phoneNumber: String
)