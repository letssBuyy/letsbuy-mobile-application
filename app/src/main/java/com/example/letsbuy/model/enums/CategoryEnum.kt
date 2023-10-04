package com.example.letsbuy.model.enums

enum class CategoryEnum(val description: String, val position: Int) {
    VEHICLES("Veículos", 1),
    ELECTRONICS("Eletrônicos", 2),
    FASHION_ACESSORIES("Acessórios", 3),
    HOUSE_DECORATION("Casa e decoração", 4),
    BOOKS("Livros", 5),
    FILMS("Filmes", 6),
    HOBBIES("Hobbies", 7),
    AUTO_PARTS("Auto peças", 8),
    SPORTS_LEISURE("Esporte e lazer", 9),
    PETS("Pets", 10),
    CHILDREN("Bebês e crianças", 11),
    NOT_MAPPED("Não mapeado", 12);

    companion object {
        fun categoryToEnum(description: String): CategoryEnum {
            return when(description) {
                VEHICLES.description -> VEHICLES
                ELECTRONICS.description -> ELECTRONICS
                FASHION_ACESSORIES.description -> FASHION_ACESSORIES
                HOUSE_DECORATION.description -> HOUSE_DECORATION
                BOOKS.description -> BOOKS
                FILMS.description -> FILMS
                HOBBIES.description -> HOBBIES
                AUTO_PARTS.description -> AUTO_PARTS
                SPORTS_LEISURE.description -> SPORTS_LEISURE
                PETS.description -> PETS
                CHILDREN.description -> CHILDREN
                else -> NOT_MAPPED
            }
        }

        fun enumCategoryToPosition(enumCategory: CategoryEnum): Int {
            return when(enumCategory) {
                VEHICLES -> VEHICLES.position
                ELECTRONICS -> ELECTRONICS.position
                FASHION_ACESSORIES -> FASHION_ACESSORIES.position
                HOUSE_DECORATION -> HOUSE_DECORATION.position
                BOOKS -> BOOKS.position
                FILMS -> FILMS.position
                HOBBIES -> HOBBIES.position
                AUTO_PARTS -> AUTO_PARTS.position
                SPORTS_LEISURE -> SPORTS_LEISURE.position
                PETS -> PETS.position
                CHILDREN  -> CHILDREN.position
                else -> NOT_MAPPED.position
            }
        }
    }
}