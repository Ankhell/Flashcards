package flashcards

import flashcards.Logger.printlnAndLog
import flashcards.Logger.readlnAndLog

val deque = CardDeque()

fun main(args: Array<String>) {
    if (args.contains("-import")) {
        deque.importCards(args[args.indexOf("-import") + 1])
        println()
    }
    menu()
    if (args.contains("-export")) {
        deque.exportCards(args[args.indexOf("-export") + 1])
    }
}

fun menu() {
    while (true) {
        askForAction()
        when (readlnAndLog()) {
            "add" -> deque.addCard()
            "remove" -> deque.removeCard()
            "import" -> deque.importCards()
            "export" -> deque.exportCards()
            "ask" -> deque.startTest()
            "log" -> Logger.saveLog()
            "hardest card" -> deque.hardestCard()
            "reset stats" -> deque.resetStats()
            "exit" -> printlnAndLog("Bye bye!").also { return }
            else -> printlnAndLog("Unknown command")
        }
        printlnAndLog()
    }
}

fun askForAction() = printlnAndLog(
    "Input the action (add, remove, import, " +
            "export, ask, exit, log, hardest card, reset stats):"
)