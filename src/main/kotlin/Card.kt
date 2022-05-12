package flashcards

class Card(val term: String, val definition: String, var misses: Int = 0) {
    operator fun component1(): String = term
    operator fun component2(): String = definition
    operator fun component3(): Int = misses

}