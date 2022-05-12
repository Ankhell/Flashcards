package flashcards

import flashcards.Logger.printlnAndLog
import flashcards.Logger.readlnAndLog
import java.io.File

fun writeTo(fileName: String? = null, successMessage: String, contentProvider: () -> String) {
    val realFileName = if (fileName != null) {
        fileName
    } else {
        printlnAndLog("File name:")
        readlnAndLog()
    }
    val file = File(realFileName)
    printlnAndLog(successMessage)
    file.writeText(contentProvider())
}