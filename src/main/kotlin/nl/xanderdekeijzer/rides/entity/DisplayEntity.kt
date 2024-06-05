package nl.xanderdekeijzer.rides.entity

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.*
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.phys.Vec3
import nl.xanderdekeijzer.rides.math.Quaternion
import nl.xanderdekeijzer.rides.utils.*
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

class DisplayEntity(
    location: Location,
    itemStack: ItemStack,
    scale: Vector,
    rotation: Quaternion,
) {
    private val id = RandomId.next()
    private val uuid = UUID.randomUUID()

    private var previousLocation: Location = location
    private var lastForceLocation: Int = 0
    private var locationChanged: Boolean = false
    private var metadataChanged: Boolean = false

    var location: Location = location
        set(value) {
            locationChanged = true
            field = value
        }
    var itemStack: ItemStack = itemStack
        set(value) {
            metadataChanged = true
            field = value
        }
    var scale: Vector = scale
        set(value) {
            metadataChanged = true
            field = value
        }
    var rotation: Quaternion = rotation
        set(value) {
            metadataChanged = true
            field = value
        }

    fun getAddEntity(): Iterable<Packet<in ClientGamePacketListener>> {
        return listOf(
            ClientboundAddEntityPacket(
                id, uuid,
                location.x, location.y, location.z,
                location.yaw, location.pitch,
                EntityType.ITEM_DISPLAY,
                0, Vec3(0.0, 0.0, 0.0),
                0.0
            ),
            ClientboundSetEntityDataPacket(
                id,
                listOf(
                    displayLeftRotation(rotation),
                    displayScale(scale),
                    itemDisplayItemStack(itemStack),
                    itemDisplayDisplayType(ItemDisplayContext.HEAD)
                )
            )
        )
    }

    fun getRemoveEntity(): ClientboundRemoveEntitiesPacket {
        return ClientboundRemoveEntitiesPacket(id)
    }

    fun getUpdateViewer(): Iterable<Packet<in ClientGamePacketListener>> {
        val packets = arrayListOf<Packet<in ClientGamePacketListener>>()

        with(packets) {
            if (metadataChanged) {
                add(
                    ClientboundSetEntityDataPacket(
                        id,
                        listOf(
                            displayLeftRotation(rotation),
                            displayScale(scale),
                            itemDisplayItemStack(itemStack),
                            itemDisplayDisplayType(ItemDisplayContext.HEAD)
                        )
                    )
                )
            }

            if (locationChanged) {
                if (location.distanceSquared(previousLocation) < 8.0 || lastForceLocation > 120) {
                    add(
                        ClientboundMoveEntityPacket.Pos(
                            id,
                            ((location.x * 32 - previousLocation.x * 32) * 128).toInt().toShort(),
                            ((location.y * 32 - previousLocation.y * 32) * 128).toInt().toShort(),
                            ((location.z * 32 - previousLocation.z * 32) * 128).toInt().toShort(),
                            false
                        )
                    )
                    previousLocation = location
                    lastForceLocation += 1

                } else {
                    val block = location.toBlockLocation()
                    add(entityTeleportPacket(id, block))
                    add(
                        ClientboundMoveEntityPacket.Pos(
                            id,
                            ((location.x * 32 - block.x * 32) * 128).toInt().toShort(),
                            ((location.y * 32 - block.y * 32) * 128).toInt().toShort(),
                            ((location.z * 32 - block.z * 32) * 128).toInt().toShort(),
                            false
                        )
                    )
                    previousLocation = location
                    lastForceLocation = 0
                }
            }
        }
        return packets
    }

}