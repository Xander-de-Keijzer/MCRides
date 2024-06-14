package nl.xanderdekeijzer.rides.utils

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket
import nl.xanderdekeijzer.rides.Main
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

/*
Iterable<Packet>
 */

//fun Iterable<Packet<in ClientGamePacketListener>>.sendPackets(players: Iterable<Player>) {
//    players.sendPackets(this)
//}
//
//fun Iterable<Packet<in ClientGamePacketListener>>.sendPackets(player: Player) {
//    player.sendPackets(this)
//}

/*
Packet
    - Iterable player
    - player
 */

fun Packet<in ClientGamePacketListener>.sendPacket(players: Iterable<Player>) {
    players.sendPacket(this)
}

fun Packet<in ClientGamePacketListener>.sendPacket(player: Player) {
    player.sendPacket(this)
}

/*
Iterable player
    - vararg packet
    - iterable packet
    - packet
 */

fun Iterable<Player>.sendPackets(vararg packets: Packet<in ClientGamePacketListener>) {
    sendPackets(packets.toList())
}

fun Iterable<Player>.sendPackets(packets: Iterable<Packet<in ClientGamePacketListener>>) {
    sendPacket(ClientboundBundlePacket(packets))
}

fun Iterable<Player>.sendPacket(packet: Packet<in ClientGamePacketListener>) {
    forEach { it.sendPacket(packet) }
}

/*
Player
    - vararg packet
    - iterable packet
    - packet
 */

fun Player.sendPackets(vararg packets: Packet<in ClientGamePacketListener>) {
    sendPackets(packets.toList())
}

fun Player.sendPackets(packets: Iterable<Packet<in ClientGamePacketListener>>) {
    sendPacket(ClientboundBundlePacket(packets))
}

fun Player.sendPacket(packet: Packet<in ClientGamePacketListener>) {
    (this as CraftPlayer).handle.connection.sendPacket(packet)
}
