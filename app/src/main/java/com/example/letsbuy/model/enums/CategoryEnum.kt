package com.example.letsbuy.model.enums

enum class CategoryEnum(val description: String) {
    VEHICLES("Veículos"),
    ELECTRONICS("Eletrônicos"),
    FASHION_ACESSORIES("Acessórios"),
    HOUSE_DECORATION("Casa e decoração"),
    BOOKS("Livros"),
    FILMS("Filmes"),
    HOBBIES("Hobbies"),
    AUTO_PARTS("Auto peças"),
    SPORTS_LEISURE("Esporte e lazer"),
    PETS("Pets"),
    CHILDREN("Bebês e crianças"),
    NOT_MAPPED("Não mapeado");

    companion object {
        fun categoryToEnum(description: String): CategoryEnum {
            return when(description) {
                "Veículos" -> VEHICLES
                "Eletrônicos" -> ELECTRONICS
                "Acessórios" -> FASHION_ACESSORIES
                "Casa e decoração" -> HOUSE_DECORATION
                "Livros" -> BOOKS
                "Filmes" -> FILMS
                "Hobbies" -> HOBBIES
                "Auto peças" -> AUTO_PARTS
                "Esporte e lazer" -> SPORTS_LEISURE
                "Pets" -> PETS
                "Bebês e crianças" -> CHILDREN
                else -> NOT_MAPPED
            }
        }
    }
}