package com.example.letsbuy.model.enums


enum class AdversimentColorEnum(val description: String, val position: Int) {
    RED("Vermelho", 1),
    GREEN("Verde", 2),
    BLUE("Azul", 3),
    YELLOW("Amarelo", 4),
    ORANGE("Laranja", 5),
    PURPLE("Roxo", 6),
    PINK("Rosa", 7),
    BLACK("Preto", 8),
    WHITE("Branco", 9),
    GRAY("Cinza", 10),
    BROWN("Marrom", 11),
    SILVER("Prata", 12),
    GOLD("Dourado", 13),
    NAVY("Azul marinho", 14),
    TEAL("Verde azulado", 15),
    NOT_MAPPED("Não mapeado", 16);


    companion object {
        fun colorToEnum(description: String): AdversimentColorEnum {
            return when(description) {
                RED.description -> RED
                GREEN.description -> GREEN
                BLUE.description -> BLUE
                YELLOW.description -> YELLOW
                ORANGE.description -> ORANGE
                PURPLE.description -> PURPLE
                PINK.description -> PINK
                BLACK.description -> BLACK
                WHITE.description -> WHITE
                GRAY.description -> GRAY
                BROWN.description -> BROWN
                SILVER.description -> SILVER
                GOLD.description -> GOLD
                NAVY.description -> NAVY
                TEAL.description -> TEAL
                else -> NOT_MAPPED
            }
        }

        fun enumColorToPosition(enumColor: AdversimentColorEnum): Int {
            return when(enumColor) {
                RED -> RED.position
                GREEN -> GREEN.position
                BLUE -> BLUE.position
                YELLOW -> YELLOW.position
                ORANGE -> ORANGE.position
                PURPLE -> PURPLE.position
                PINK -> PINK.position
                BLACK -> BLACK.position
                WHITE -> WHITE.position
                GRAY -> GRAY.position
                BROWN -> BROWN.position
                SILVER -> SILVER.position
                GOLD -> GOLD.position
                NAVY -> NAVY.position
                TEAL -> TEAL.position
                else -> NOT_MAPPED.position
            }
        }

        fun enumColorToDescription(colorEnum: AdversimentColorEnum): String {
            return when (colorEnum) {
                AdversimentColorEnum.RED -> AdversimentColorEnum.RED.description
                AdversimentColorEnum.GREEN -> AdversimentColorEnum.GREEN.description
                AdversimentColorEnum.BLUE -> AdversimentColorEnum.BLUE.description
                AdversimentColorEnum.YELLOW -> AdversimentColorEnum.YELLOW.description
                AdversimentColorEnum.ORANGE -> AdversimentColorEnum.ORANGE.description
                AdversimentColorEnum.PURPLE -> AdversimentColorEnum.PURPLE.description
                AdversimentColorEnum.PINK -> AdversimentColorEnum.PINK.description
                AdversimentColorEnum.BLACK -> AdversimentColorEnum.BLACK.description
                AdversimentColorEnum.WHITE -> AdversimentColorEnum.WHITE.description
                AdversimentColorEnum.GRAY -> AdversimentColorEnum.GRAY.description
                AdversimentColorEnum.BROWN -> AdversimentColorEnum.BROWN.description
                AdversimentColorEnum.SILVER -> AdversimentColorEnum.SILVER.description
                AdversimentColorEnum.GOLD -> AdversimentColorEnum.GOLD.description
                AdversimentColorEnum.NAVY -> AdversimentColorEnum.NAVY.description
                AdversimentColorEnum.TEAL -> AdversimentColorEnum.TEAL.description
                AdversimentColorEnum.NOT_MAPPED -> AdversimentColorEnum.NOT_MAPPED.description
            }
        }
    }
}