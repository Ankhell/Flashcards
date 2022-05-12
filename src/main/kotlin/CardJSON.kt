package flashcards

object CardSerializer {

    private fun String.indentBlock(indent: Int = 1, indentSymbol: String = "\t"): String =
        this.split("\n").joinToString("\n${indentSymbol.repeat(indent)}", indentSymbol.repeat(indent))

    fun serializeAll(cardDeque: CardDeque): String =
        cardDeque.getAllCards().joinToString(",\n", "[\n", "\n]") { serialize(it).indentBlock() }

    private fun serialize(card: Card): String {
        return """
                {
                ${"\t"}"term": "${card.term}",
                ${"\t"}"definition": "${card.definition}",
                ${"\t"}"misses": ${card.misses}
                }
            """.trimIndent()
    }
}

object CardDeserializer {

    private fun deserializeCard(serializedCard: String): Card {
        val termRegex = "\"term\": \"(.+?)\"".toRegex()
        val term = termRegex.find(serializedCard)?.groups?.get(1)?.value ?: ""
        val definitionRegex = "\"definition\": \"(.+?)\"".toRegex()
        val definition = definitionRegex.find(serializedCard)?.groups?.get(1)?.value ?: ""
        val missesRegex = "\"misses\": (\\d+?)".toRegex()
        val misses = missesRegex.find(serializedCard)?.groups?.get(1)?.value?.toInt() ?: 0
        return Card(term, definition, misses)
    }

    fun deserializeAllCards(serializedCardArray: String): List<Card> =
        serializedCardArray.replace("[\\[\\]]".toRegex(), "").split("},")
                .map { deserializeCard(it) }
                .toList()

}