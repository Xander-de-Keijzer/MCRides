package nl.xanderdekeijzer.rides.active

data class LogItem(
    val type: LogType,
    val message: String,
    val read: Boolean = false
)
