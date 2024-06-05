package nl.xanderdekeijzer.rides.data

import nl.xanderdekeijzer.rides.math.Quaternion
import org.bukkit.util.Vector

data class Node(
    val id: Int,
    val location: Vector,
    val rotation: Vector = Vector(),
    var radius: Double = 0.0,
    val connections: ArrayList<Int> = arrayListOf()
) {

    fun generateControlPoints(): Pair<Vector, Vector> {
        val rotation = Quaternion.fromYawPitchRoll(rotation)
        val localCP1 = Vector(radius, 0.0, 0.0)
        val localCP2 = Vector(-radius, 0.0, 0.0)
        val worldCP1 = rotation.rotate(localCP1).add(location)
        val worldCP2 = rotation.rotate(localCP2).add(location)
        return worldCP1 to worldCP2
    }
}
