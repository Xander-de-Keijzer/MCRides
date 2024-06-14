package nl.xanderdekeijzer.rides.utils

import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.Optional

@Suppress("UNCHECKED_CAST")
private fun <T> entityDataAccessor(clazz: Class<*>, fieldName: String): EntityDataAccessor<T> {
    val field = try {
        clazz.getDeclaredField(fieldName)
    } catch (_: NoSuchFieldException) {
        try {
            clazz.getField(fieldName)
        } catch (_: NoSuchFieldException) {
            throw NoSuchFieldException("Field: $fieldName, Name: ${clazz.name}, Declared: ${clazz.declaredFields.map { it.name }.contains(fieldName)} Fields: ${clazz.fields.map { it.name }.contains(fieldName)}")
        }
    }
    if (!field.canAccess(null)) field.isAccessible = true

    return field.get(null) as EntityDataAccessor<T>
}

fun <T: Any> EntityDataAccessor<T>.create(value: T): SynchedEntityData.DataValue<T>
    = SynchedEntityData.DataValue.create<T>(this, value)

// Entity
val DATA_SHARED_FLAGS_ID: EntityDataAccessor<Byte> = entityDataAccessor(Entity::class.java, "DATA_SHARED_FLAGS_ID")
val DATA_AIR_SUPPLY_ID: EntityDataAccessor<Integer> = entityDataAccessor(Entity::class.java, "DATA_AIR_SUPPLY_ID")
val DATA_CUSTOM_NAME: EntityDataAccessor<Optional<Component>> = entityDataAccessor(Entity::class.java, "DATA_CUSTOM_NAME")
val DATA_CUSTOM_NAME_VISIBLE: EntityDataAccessor<Boolean> = entityDataAccessor(Entity::class.java, "DATA_CUSTOM_NAME_VISIBLE")
val DATA_SILENT: EntityDataAccessor<Boolean> = entityDataAccessor(Entity::class.java, "DATA_SILENT")
val DATA_NO_GRAVITY: EntityDataAccessor<Boolean> = entityDataAccessor(Entity::class.java, "DATA_NO_GRAVITY")
val DATA_POSE: EntityDataAccessor<net.minecraft.world.entity.Pose> = entityDataAccessor(Entity::class.java, "DATA_POSE")
val DATA_TICKS_FROZEN: EntityDataAccessor<Integer> = entityDataAccessor(Entity::class.java, "DATA_TICKS_FROZEN")

// Display
val DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID: EntityDataAccessor<Integer> = entityDataAccessor(Display::class.java, "DATA_TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS_ID")
val DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID: EntityDataAccessor<Integer> = entityDataAccessor(Display::class.java, "DATA_TRANSFORMATION_INTERPOLATION_DURATION_ID")
val DATA_POS_ROT_INTERPOLATION_DURATION_ID: EntityDataAccessor<Integer> = entityDataAccessor(Display::class.java, "DATA_POS_ROT_INTERPOLATION_DURATION_ID")
val DATA_TRANSLATION_ID: EntityDataAccessor<Vector3f> = entityDataAccessor(Display::class.java, "DATA_TRANSLATION_ID")
val DATA_SCALE_ID: EntityDataAccessor<Vector3f> = entityDataAccessor(Display::class.java, "DATA_SCALE_ID")
val DATA_LEFT_ROTATION_ID: EntityDataAccessor<Quaternionf> = entityDataAccessor(Display::class.java, "DATA_LEFT_ROTATION_ID")
val DATA_RIGHT_ROTATION_ID: EntityDataAccessor<Quaternionf> = entityDataAccessor(Display::class.java, "DATA_RIGHT_ROTATION_ID")
val DATA_BILLBOARD_RENDER_CONSTRAINTS_ID: EntityDataAccessor<Byte> = entityDataAccessor(Display::class.java, "DATA_BILLBOARD_RENDER_CONSTRAINTS_ID")
val DATA_BRIGHTNESS_OVERRIDE_ID: EntityDataAccessor<Integer> = entityDataAccessor(Display::class.java, "DATA_BRIGHTNESS_OVERRIDE_ID")
val DATA_VIEW_RANGE_ID: EntityDataAccessor<Float> = entityDataAccessor(Display::class.java, "DATA_VIEW_RANGE_ID")
val DATA_SHADOW_RADIUS_ID: EntityDataAccessor<Float> = entityDataAccessor(Display::class.java, "DATA_SHADOW_RADIUS_ID")
val DATA_SHADOW_STRENGTH_ID: EntityDataAccessor<Float> = entityDataAccessor(Display::class.java, "DATA_SHADOW_STRENGTH_ID")
val DATA_WIDTH_ID: EntityDataAccessor<Float> = entityDataAccessor(Display::class.java, "DATA_WIDTH_ID")
val DATA_HEIGHT_ID: EntityDataAccessor<Float> = entityDataAccessor(Display::class.java, "DATA_HEIGHT_ID")
val DATA_GLOW_COLOR_OVERRIDE_ID: EntityDataAccessor<Integer> = entityDataAccessor(Display::class.java, "DATA_GLOW_COLOR_OVERRIDE_ID")

// Item display
val DATA_ITEM_STACK_ID: EntityDataAccessor<ItemStack> = entityDataAccessor(Display.ItemDisplay::class.java, "DATA_ITEM_STACK_ID")
val DATA_ITEM_DISPLAY_ID: EntityDataAccessor<Byte> = entityDataAccessor(Display.ItemDisplay::class.java, "DATA_ITEM_DISPLAY_ID")

// Text display
val DATA_TEXT_ID: EntityDataAccessor<Component> = entityDataAccessor(Display.TextDisplay::class.java, "DATA_TEXT_ID")
val DATA_LINE_WIDTH_ID: EntityDataAccessor<Integer> = entityDataAccessor(Display.TextDisplay::class.java, "DATA_LINE_WIDTH_ID")
val DATA_BACKGROUND_COLOR_ID: EntityDataAccessor<Integer> = entityDataAccessor(Display.TextDisplay::class.java, "DATA_BACKGROUND_COLOR_ID")
val DATA_TEXT_OPACITY_ID: EntityDataAccessor<Byte> = entityDataAccessor(Display.TextDisplay::class.java, "DATA_TEXT_OPACITY_ID")
val DATA_STYLE_FLAGS_ID: EntityDataAccessor<Byte> = entityDataAccessor(Display.TextDisplay::class.java, "DATA_STYLE_FLAGS_ID")
        