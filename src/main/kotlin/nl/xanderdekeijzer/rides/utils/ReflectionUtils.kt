package nl.xanderdekeijzer.rides.utils

import io.netty.buffer.Unpooled
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData.DataValue
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Display.ItemDisplay
import net.minecraft.world.item.ItemDisplayContext
import nl.xanderdekeijzer.rides.math.Quaternion
import org.bukkit.Location
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f


fun entityTeleportPacket(id: Int, location: Location): ClientboundTeleportEntityPacket
        = entityTeleportPacket(id, location.x, location.y, location.z, location.yaw, location.pitch)

fun entityTeleportPacket(id: Int, location: Vector): ClientboundTeleportEntityPacket
        = entityTeleportPacket(id, location.x, location.y, location.z)

fun entityTeleportPacket(id: Int, x: Double, y: Double, z: Double, yaw: Float = 0F, pitch: Float = 0F): ClientboundTeleportEntityPacket {
    val buf = FriendlyByteBuf(Unpooled.buffer())
    buf.writeVarInt(id)
    buf.writeDouble(x)
    buf.writeDouble(y)
    buf.writeDouble(z)
    buf.writeByte(yaw.toInt())
    buf.writeByte(pitch.toInt())
    buf.writeBoolean(false)

    return ClientboundTeleportEntityPacket.STREAM_CODEC.decode(buf)
}

fun ItemStack.nms(): net.minecraft.world.item.ItemStack = (this as CraftItemStack).handle

fun Any.f(name: String): Any {
    return this.javaClass.field(name, this)
}

fun Class<*>.field(name: String, instance: Any? = null): Any {
    try {
        val field = this.getField(name)
        field.isAccessible = true
        return field.get(instance)
    } catch (_: NoSuchFieldException) {
        return declaredField(name, instance)
    }
}

fun Class<*>.declaredField(name: String, instance: Any? = null): Any {
    try {
        val field = this.getDeclaredField(name)
        field.isAccessible = true
        return field.get(instance)
    } catch (_: NoSuchFieldException) {
        return method("get" + name.replaceFirstChar(Char::titlecase), instance=instance)
    }
}

fun Any.m(name: String, vararg args: Any): Any {
    return this.javaClass.method(name, *args, this)
}

fun Class<*>.method(name: String, vararg args: Any, instance: Any? = null): Any {
    val types = args.map { it.javaClass }.toTypedArray()
    try {
        val method = this.getMethod(name, *types)
        method.isAccessible = true
        return method.invoke(instance, *args)
    } catch (_: NoSuchMethodException) {
        return declaredMethod(name, args, instance=instance)
    }
}

fun Class<*>.declaredMethod(name: String, vararg args: Any, instance: Any? = null): Any {
    val types = args.map { it.javaClass }.toTypedArray()
    val method = this.getDeclaredMethod(name, *types)
    method.isAccessible = true
    return method.invoke(instance, *args)
}

operator fun Any.get(index: Int): Any {
    return (this as List<*>)[index] ?: throw IndexOutOfBoundsException("Index $index is out of bounds for list $this[${this.size}].")
}

fun Class<*>?.newInstance(): Any? {
    return this?.getDeclaredConstructor()?.newInstance() ?: this?.getConstructor()?.newInstance()
}
