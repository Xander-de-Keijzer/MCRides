package nl.xanderdekeijzer.rides.math

import org.bukkit.util.Vector
import org.joml.Quaterniond
import org.joml.Quaternionf
import kotlin.math.abs
import kotlin.math.sqrt

fun Vector.toQuaternion(): Quaternion {
    return Quaternion.fromYawPitchRoll(this)
}

fun Quaternionf.toQuaternion(): Quaternion {
    return Quaternion(this.x.toDouble(), this.y.toDouble(), this.z.toDouble(), this.w.toDouble())
}

fun Quaterniond.toQuaternion(): Quaternion {
    return Quaternion(this.x, this.y, this.z, this.w)
}

fun Double.toRadians(): Double {
    return Math.toRadians(this)
}

fun Double.toDegrees(): Double {
    return Math.toDegrees(this)
}

fun Double.asNormFactor(): Double {
    // https://stackoverflow.com/a/12934750
    return if (abs(1.0 - this) < 2.107342e-08) {
        2.0 / (1.0 + this)
    } else {
        1.0 / sqrt(this)
    }
}

fun halfCosTan2(y: Double, x: Double): Double {
    var tmp = y / x
    tmp *= tmp
    tmp += 1.0
    return (if ((x < 0.0)) -0.5 else 0.5) / sqrt(tmp)
}

fun Any?.unit() = Unit
