package com.example.letsbuy.model.enums

enum class QualityEnum(val description: String) {
    NEW("Novo"),
    SEMI_NEW("Semi Novo"),
    USED("Usado"),
    DEFECTIVE("Defeituoso"),
    NOT_MAPPED("Não mapeado");

    companion object {
        fun qualityToEnum(description: String): QualityEnum {
            return when(description) {
                "Novo" -> NEW
                "Semi Novo" -> SEMI_NEW
                "Usado" -> USED
                "Defeituoso" -> DEFECTIVE
                else -> NOT_MAPPED
            }
        }
    }
}