package nl.xanderdekeijzer.rides.active
//
//import nl.xanderdekeijzer.rides.track.Track
//
class Ride(
    val name: String,
)
//{
//    private val controller: RideController = RideController(this)
//    private val tracks: ArrayList<Track> = arrayListOf()
//
//    val hasEmergency: Boolean
//        get() = controller.hasEmergency
//
//    operator fun get(id: String): Track? {
//        return tracks.find { it.id == id }
//    }
//
//    fun logEmergency(message: String) { controller.logEmergency(message) }
//    fun logWarning(message: String) { controller.logWarning(message) }
//    fun logInfo(message: String) { controller.logInfo(message) }
//    fun emergencyStop(reason: String) { controller.emergencyStop(reason) }
//    fun resetEmergency(origin: String) { controller.resetEmergency(origin) }
//
//}