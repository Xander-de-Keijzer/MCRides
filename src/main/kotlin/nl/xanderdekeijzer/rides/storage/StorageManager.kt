package nl.xanderdekeijzer.rides.storage

import nl.xanderdekeijzer.rides.Main
import nl.xanderdekeijzer.rides.active.Ride
import nl.xanderdekeijzer.rides.data.AttachmentOffset
import nl.xanderdekeijzer.rides.data.CartData
import nl.xanderdekeijzer.rides.math.Quaternion
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.util.Vector
import java.io.*
import javax.swing.text.Segment

/*

    Utils

 */

fun binaryWriter(fileName: String, handle: DataOutputStream.() -> Unit) {
    val file = File(fileName)
    val outputStream = FileOutputStream(file)
    val dataStream = DataOutputStream(outputStream)
    dataStream.handle()
}

fun <T> binaryReader(fileName: String, handle: DataInputStream.() -> T): T {
    val file = File(fileName)
    val inputStream = FileInputStream(file)
    val dataStream = DataInputStream(inputStream)
    return dataStream.handle()
}

/*

    Extensions

 */

fun DataOutputStream.writeVector(vector: Vector) {
    writeDouble(vector.x)
    writeDouble(vector.y)
    writeDouble(vector.z)
}

fun DataInputStream.readVector(): Vector {
    val x = readDouble()
    val y = readDouble()
    val z = readDouble()
    return Vector(x, y, z)
}


fun DataOutputStream.writeQuaternion(quaternion: Quaternion) {
    writeDouble(quaternion.x)
    writeDouble(quaternion.y)
    writeDouble(quaternion.z)
    writeDouble(quaternion.w)
}

fun DataInputStream.readQuaternion(): Quaternion {
    val x = readDouble()
    val y = readDouble()
    val z = readDouble()
    val w = readDouble()
    return Quaternion(x, y, z, w)
}


fun DataOutputStream.writeLocation(location: Location) {
    val hasWorld = location.world != null
    writeBoolean(hasWorld)
    if (hasWorld) writeUTF(location.world.name)
    writeDouble(location.x)
    writeDouble(location.y)
    writeDouble(location.z)
    writeFloat(location.yaw)
    writeFloat(location.pitch)
}

fun DataInputStream.readLocation(): Location {
    val hasWorld = readBoolean()
    val world = if (hasWorld) Bukkit.getWorld(readUTF()) else null
    val x = readDouble()
    val y = readDouble()
    val z = readDouble()
    val yaw = readFloat()
    val pitch = readFloat()
    return Location(world, x, y, z, yaw, pitch)
}


fun DataOutputStream.writeAttachmentOffset(offset: AttachmentOffset) {
    writeVector(offset.translation)
    writeVector(offset.rotation)
}

fun DataInputStream.readAttachmentOffset(): AttachmentOffset {
    val translation = readVector()
    val rotation = readVector()
    return AttachmentOffset(translation, rotation)
}


fun DataOutputStream.writeCartData(cartData: CartData) {
    writeUTF(cartData.material.name)
    writeInt(cartData.modelData)
    writeDouble(cartData.sizeFront)
    writeDouble(cartData.sizeRear)
    writeDouble(cartData.weight)
    writeInt(cartData.seats.size)
    cartData.seats.forEach { writeAttachmentOffset(it) }
}

fun DataInputStream.readCartData(): CartData {
    val material = Material.getMaterial(readUTF())!!
    val modelData = readInt()
    val sizeFront = readDouble()
    val sizeRear = readDouble()
    val weight = readDouble()
    val seatCount = readInt()
    val seats = (0..<seatCount).map { readAttachmentOffset() }.toMutableList() as ArrayList<AttachmentOffset>
    return CartData(material, modelData, sizeFront, sizeRear, weight, seats)
}

fun DataOutputStream.writeRide(ride: Ride) {

}

//
//fun DataOutputStream.writeTrackSegment(trackSegment: TrackSegment) {
//    writeUTF(trackSegment.id)
//    writeInt(trackSegment.startNode)
//    writeInt(trackSegment.endNode)
//    writeInt(trackSegment.modules.size)
//    trackSegment.modules.forEach {
//        writeUTF(Main.registry.getTrackModuleName(it) ?:
//        throw IllegalStateException(
//            "Attempting to write module that was not registered (${it.javaClass}), " +
//            "please register it using 'registry.trackModuleMap'."
//        )) }
//}
//
//fun DataInputStream.readTrackSegment(): TrackSegment {
//    val id = readUTF()
//    val startNode = readInt()
//    val endNode = readInt()
//    val moduleCount = readInt()
//    val modules = (0..<moduleCount).map {
//        readUTF().let {
//            Main.registry.createTrackModule(it) ?: throw IllegalStateException(
//                "Attempting to read module that was not registered ($it), " +
//                "please register it using 'registry.trackModuleMap"
//            )
//        }
//    }.toMutableList() as ArrayList<TrackModule>
//    return TrackSegment(startNode, endNode, modules, id)
//}
