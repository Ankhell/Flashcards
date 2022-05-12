package flashcards

import flashcards.Logger.printlnAndLog
import flashcards.Logger.readlnAndLog
import java.io.File
import kotlin.random.Random

class CardDeque {
    private val cards: MutableList<Card> = mutableListOf()

    fun getAllCards(): List<Card> = cards.toList()

    fun addCard() {
        printlnAndLog("The card:")
        val term = readlnAndLog().trim()
        if (hasTerm(term)) {
            printlnAndLog("The card \"$term\" already exists.")
            return
        }
        printlnAndLog("The definition of the card:")
        val definition = readlnAndLog().trim()
        if (hasDefinition(definition)) {
            printlnAndLog("The definition \"$definition\" already exists.")
            return
        }
        cards.add(Card(term, definition))
        printlnAndLog("The pair (\"$term\":\"$definition\") has been added")
    }

    fun removeCard() {
        printlnAndLog("Which card?")
        val term = readlnAndLog()
        if (hasTerm(term)) {
            cards.removeIf { it.term == term }
            printlnAndLog("The card has been removed.")
            return
        }
        printlnAndLog("Can't remove \"$term\": there is no such card.")
    }

    fun importCards(fileName: String) {
        val file = File(fileName)
        if (!file.exists()) {
            printlnAndLog("File not found.")
            return
        }
        val cardsToAdd = CardDeserializer.deserializeAllCards(file.readText())
        cardsToAdd.forEach { importedCard -> cards.removeIf { existentCard -> existentCard.term == importedCard.term } }
        cards.addAll(cardsToAdd)
        printlnAndLog("${cardsToAdd.size} cards have been loaded.")
    }

    fun importCards() {
        printlnAndLog("File name:")
        val fileName = readlnAndLog()
        importCards(fileName)
    }

    fun exportCards(fileName: String) {
        writeTo(fileName = fileName, successMessage = "${cards.size} cards have been saved") {
            CardSerializer.serializeAll(this)
        }
    }

    fun exportCards() {
        writeTo(successMessage = "${cards.size} cards have been saved") {
            CardSerializer.serializeAll(this)
        }
    }

    fun startTest() {
        if (cards.isEmpty()) {
            println("No cards found")
            return
        }
        printlnAndLog("How many times to ask?")
        val timesToAsk = readlnAndLog().toInt()
        repeat(timesToAsk) {
            val cardIndex = if (cards.lastIndex != 0) Random.nextInt(0, cards.lastIndex) else 0
            cards[cardIndex].let {
                printlnAndLog("Print the definition of \"${it.term}\":")
                val userInputDefinition = readlnAndLog()
                printlnAndLog(
                    if (userInputDefinition == it.definition) {
                        "Correct!"
                    } else {
                        it.misses++
                        "Wrong. The right answer is ${getDefinitionTip(it.definition, userInputDefinition)}"
                    }
                )
            }
        }
    }

    fun hardestCard() {
        if (thereIsNoMisses()) {
            printlnAndLog("There are no cards with errors.")
            return
        }
        val maxMisses = getMaxMisses()
        val missedCards = cards.filter { it.misses == maxMisses }
        val missedCardsString = missedCards.joinToString("\", \"", "\"", "\"") { it.term }
        printlnAndLog(
            "The hardest card${if (missedCards.size > 1) "s are" else " is"} $missedCardsString. " +
                    "You have $maxMisses errors answering ${if (missedCards.size > 1) "them" else "it"}"
        )
    }

    fun resetStats() =
        cards.forEach { it.misses = 0 }.also { printlnAndLog("Card statistics have been reset.") }


    private fun getMaxMisses(): Int = cards.maxOf { it.misses }

    private fun thereIsNoMisses(): Boolean = cards.none { it.misses != 0 }

    private fun getDefinitionTip(definition: String, userInputDefinition: String) =
        if (hasDefinition(userInputDefinition)) {
            "\"$definition\", but your definition is correct for \"${getTermByDefinition(userInputDefinition)}\"."
        } else "\"$definition\"."

    private fun getTermByDefinition(definition: String): String? =
        cards.firstOrNull { it.definition == definition }?.term

    private fun hasTerm(term: String): Boolean {
        return cards.any { it.term == term }
    }

    private fun hasDefinition(definition: String): Boolean {
        return cards.any { it.definition == definition }
    }
}