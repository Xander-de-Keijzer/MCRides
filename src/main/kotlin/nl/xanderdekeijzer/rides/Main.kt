package nl.xanderdekeijzer.rides

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.world.entity.EntityType
import net.minecraft.world.phys.Vec3
import nl.xanderdekeijzer.rides.math.Quaternion
import nl.xanderdekeijzer.rides.utils.*
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


@Suppress("UnstableApiUsage")
class Main: JavaPlugin(), Listener {

    companion object {
        lateinit var instance: JavaPlugin

        val globalScheduler: GlobalRegionScheduler = instance.server.globalRegionScheduler
        val asyncScheduler: AsyncScheduler = instance.server.asyncScheduler
        val playerData: HashMap<Player, PlayerData> = hashMapOf()
    }

    override fun onEnable() {
        instance = this
        server.onlinePlayers.forEach { playerData[it] = PlayerData() }

        getCommand("ride")?.setExecutor(this)

        logger.info("${pluginMeta.displayName} enabled")
    }

    override fun onDisable() {
        logger.info("${pluginMeta.displayName} disabled")
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        playerData[event.player] = PlayerData()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        playerData.remove(event.player)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (sender is Player) {

            val location = sender.location
            val id = RandomId.next()

            sender.sendPackets(
                ClientboundAddEntityPacket(
                    id,
                    UUID.randomUUID(),
                    location.x,
                    location.y,
                    location.z,
                    0f,
                    0f,
                    EntityType.ITEM_DISPLAY,
                    0,
                    Vec3(0.0, 0.0, 0.0),
                    0.0
                ),
                ClientboundSetEntityDataPacket(
                    id,
                    listOf(itemDisplayItemStack(ItemStack(Material.GRAY_CONCRETE)))
                )
            )

            var yaw = 0.0
            var pitch = 0.0
            var roll = 0.0

            runSyncTimer { cancel ->
                val quat = Quaternion.fromYawPitchRoll(yaw, pitch, roll)

                ClientboundSetEntityDataPacket(
                    id,
                    listOf(displayLeftRotation(quat))
                ).sendPacket(sender)


                if (yaw < 45) {
                    yaw += 1
                } else if (pitch < 45) {
                    pitch += 1
                } else if (roll < 45) {
                    roll += 1
                } else {
                    cancel()
                }
            }

            runSyncLater(400) {
                ClientboundRemoveEntitiesPacket(id).sendPacket(sender)
            }

        }

        return true
    }


}