package nl.xanderdekeijzer.rides.utils

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener

data class PlayerData(
    val packetBuffer: ArrayList<Packet<in ClientGamePacketListener>> = arrayListOf()
)
