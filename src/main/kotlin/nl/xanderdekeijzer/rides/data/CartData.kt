package nl.xanderdekeijzer.rides.data

import org.bukkit.Material

data class CartData(
    var material: Material = Material.DIAMOND_AXE,
    var modelData: Int = 0,
    var sizeFront: Double = 1.0,
    var sizeRear: Double = 1.0,
    var weight: Double = 100.0,
    val seats: ArrayList<AttachmentOffset> = arrayListOf(),
)
