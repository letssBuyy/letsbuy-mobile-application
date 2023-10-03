package com.example.letsbuy.model.enums

import java.lang.Exception

enum class AdversimentColorEnum(val description: String) {
    RED("Vermelho"),
    GREEN("Verde"),
    BLUE("Azul"),
    YELLOW("Amarelo"),
    ORANGE("Laranja"),
    PURPLE("Roxo"),
    PINK("Rosa"),
    BLACK("Preto"),
    WHITE("Branco"),
    GRAY("Cinza"),
    BROWN("Marrom"),
    SILVER("Prata"),
    GOLD("Dourado"),
    NAVY("Azul marinho"),
    TEAL("Verde azulado"),
    NOT_MAPPED("Não mapeado");


    companion object {
        fun colorToEnum(description: String): AdversimentColorEnum {
            return when(description) {
                "Vermelho" -> RED
                "Verde" -> GREEN
                "Azul" -> BLUE
                "Amarelo" -> YELLOW
                "Laranja" -> ORANGE
                "Roxo" -> PURPLE
                "Rosa" -> PINK
                "Preto" -> BLACK
                "Branco" -> WHITE
                "Cinza" -> GRAY
                "Marrom" -> BROWN
                "Prata" -> SILVER
                "Dourado" -> GOLD
                "Azul marinho" -> NAVY
                "Verde azulado" -> TEAL
                else -> NOT_MAPPED
            }
        }
    }
}