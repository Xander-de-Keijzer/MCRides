package nl.xanderdekeijzer.rides.active

class RideController(val ride: Ride) {

    var autoEnabled: Boolean = false
    var emergencyStop: String? = null
    val hasEmergency: Boolean
        get() = emergencyStop != null
    val log: ArrayList<LogItem> = arrayListOf()

    fun logEmergency(message: String) { log.add(LogItem(LogType.EMERGENCY, message)) }
    fun logWarning(message: String) { log.add(LogItem(LogType.WARNING, message)) }
    fun logInfo(message: String) { log.add(LogItem(LogType.INFO, message)) }

    fun emergencyStop(reason: String) {
        autoEnabled = false
        if (!hasEmergency) emergencyStop = reason
        logEmergency(reason)
    }

    fun resetEmergency(origin: String) {
        emergencyStop = null
        logInfo("Emergency was reset ($origin).")
    }

    fun startAuto() {
        autoEnabled = true
    }

    fun stopAuto() {
        autoEnabled = false
    }

}