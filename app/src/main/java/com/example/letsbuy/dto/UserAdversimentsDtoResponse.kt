package com.example.letsbuy.dto

data class UserAdversimentsDtoResponse(
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
     val quantityTotalAdversiment: Long,
     val quantityTotalSolded: Long,
     val quantityTotalActive: Long,
     val balance: Double,
     var bankAccount: BankAccount? = null,
     val accessLevel: String,
     val adversiments: List<UserAdversimentsDto>
)