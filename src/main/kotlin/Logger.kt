package flashcards

object Logger {

    private val logLines = mutableListOf<String>()

    fun printlnAndLog(output: String = "") = output.also { logLines.add(it) }.also(::println)

    fun readlnAndLog(): String = readln().also { logLines.add(it) }

    fun saveLog() {
        writeTo(successMessage = "The log has been saved.") { logLines.joinToString("\n") }
    }
}