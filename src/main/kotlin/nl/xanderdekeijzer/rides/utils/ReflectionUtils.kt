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

val DISPLAY_LEFT_ROTATION_ID = Display::class.java.field("DATA_LEFT_ROTATION_ID").f("id") as Int
val DISPLAY_SCALE_ID = Display::class.java.field("DATA_SCALE_ID").f("id") as Int
val ITEM_DISPLAY_ITEM_STACK_ID = ItemDisplay::class.java.field("DATA_ITEM_STACK_ID").f("id") as Int
val ITEM_DISPLAY_ITEM_DISPLAY_ID = ItemDisplay::class.java.field("DATA_ITEM_DISPLAY_ID").f("id") as Int

fun displayLeftRotation(rotation: Quaternion): DataValue<Quaternionf> =
    DataValue.create(
        EntityDataAccessor(
            DISPLAY_LEFT_ROTATION_ID,
            EntityDataSerializers.QUATERNION
        ),
        rotation.toQuaternionF()
    )
fun displayScale(scale: Vector): DataValue<Vector3f> =
    DataValue.create(
        EntityDataAccessor(
            DISPLAY_SCALE_ID,
            EntityDataSerializers.VECTOR3
        ),
        scale.toVector3f()
    )
fun itemDisplayItemStack(itemStack: ItemStack): DataValue<net.minecraft.world.item.ItemStack> =
    DataValue.create(
        EntityDataAccessor(
            ITEM_DISPLAY_ITEM_STACK_ID,
            EntityDataSerializers.ITEM_STACK
        ),
        (itemStack as CraftItemStack).handle
    )
fun itemDisplayDisplayType(displayType: ItemDisplayContext): DataValue<Byte> =
    DataValue.create(
        EntityDataAccessor(
            ITEM_DISPLAY_ITEM_DISPLAY_ID,
            EntityDataSerializers.BYTE
        ),
        displayType.id
    )

fun entityTeleportPacket(id: Int, location: Location): ClientboundTeleportEntityPacket {
    val buf = FriendlyByteBuf(Unpooled.buffer())
    buf.writeVarInt(id)
    buf.writeDouble(location.x)
    buf.writeDouble(location.y)
    buf.writeDouble(location.z)
    buf.writeByte(location.yaw.toInt())
    buf.writeByte(location.pitch.toInt())
    buf.writeBoolean(false)

    return ClientboundTeleportEntityPacket::class.java
        .getConstructor(FriendlyByteBuf::class.java)
        .newInstance(
            buf
        )
}

fun Any.f(name: String): Any {
    return this.javaClass.field(name, this)
}

fun Class<*>.field(name: String, instance: Any? = null): Any {
    try {
        val field = this.getField(name)
        field.isAccessible = true
        return field.get(instance)
    } catch (e: NoSuchFieldException) {
        return declaredField(name, instance)
    }
}

fun Class<*>.declaredField(name: String, instance: Any? = null): Any {
    try {
        val field = this.getDeclaredField(name)
        field.isAccessible = true
        return field.get(instance)
    } catch (e: NoSuchFieldException) {
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
    } catch (e: NoSuchMethodException) {
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
    return (this as List<*>)[index] ?: throw IndexOutOfBoundsException("Index $index is out of bounds for list $this.")
}

fun Class<*>?.newInstance(): Any? {
    return this?.getDeclaredConstructor()?.newInstance()
}
