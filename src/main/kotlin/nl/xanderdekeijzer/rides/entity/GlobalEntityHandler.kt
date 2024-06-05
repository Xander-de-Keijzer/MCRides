package nl.xanderdekeijzer.rides.entity

import nl.xanderdekeijzer.rides.Main
import nl.xanderdekeijzer.rides.utils.bufferPacket
import nl.xanderdekeijzer.rides.utils.bufferPackets
import org.bukkit.entity.Player

class GlobalEntityHandler {
    private val VIEW_DISTANCE = 10 * 10
    private val viewerMap: HashMap<DisplayEntity, MutableList<Player>> = hashMapOf()

    fun registerEntity(entity: DisplayEntity, viewingDistance: Double) {
        val distance = viewingDistance * viewingDistance
        val viewers = Main.instance.server.onlinePlayers.filter {
            entity.location.distanceSquared(it.location) < distance
        }.toMutableList()

        viewers.forEach {
            Main.playerData[it]?.packetBuffer?.addAll(entity.getAddEntity())
        }
        viewerMap[entity] = viewers
    }

    fun removeEntity(entity: DisplayEntity) {
        viewerMap[entity]?.forEach {
            Main.playerData[it]?.packetBuffer?.add(entity.getRemoveEntity())
        }
        viewerMap.remove(entity)
    }

    fun tick() {
        for ((entity, curViewers) in viewerMap) {
            val newViewers = Main.instance.server.onlinePlayers.filter {
                entity.location.distanceSquared(it.location) < VIEW_DISTANCE
            }.toMutableList()

            for (viewer in newViewers) {
                if (viewer !in curViewers) {
                    viewer.bufferPackets(entity.getAddEntity())
                    curViewers.add(viewer)
                } else {
                    viewer.bufferPackets(entity.getUpdateViewer())
                }
            }
            for (viewer in curViewers) {
                if (viewer !in newViewers) {
                    viewer.bufferPacket(entity.getRemoveEntity())
                    curViewers.remove(viewer)
                }
            }

        }
    }
}