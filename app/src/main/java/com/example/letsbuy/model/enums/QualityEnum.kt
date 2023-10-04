package com.example.letsbuy.model.enums

enum class QualityEnum(val description: String, val position: Int) {
    NEW("Novo", 1),
    SEMI_NEW("Semi Novo", 2),
    USED("Usado", 3),
    DEFECTIVE("Defeituoso", 4),
    NOT_MAPPED("Não mapeado", 5);

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

        fun enumQualityToPosition(enumQuality: QualityEnum): Int {
            return when(enumQuality) {
                NEW -> 1
                SEMI_NEW -> 2
                USED -> 3
                DEFECTIVE -> 4
                else -> 5
            }
        }
    }
}