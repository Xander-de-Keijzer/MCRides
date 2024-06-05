package nl.xanderdekeijzer.rides.math

import org.bukkit.util.Vector
import org.joml.Quaterniond
import org.joml.Quaternionf
import kotlin.math.*

/**
 * A quaternion for performing rotations in 3D space.
 * The quaternion is automatically normalized.
 */
data class Quaternion(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var w: Double = 1.0
): Cloneable {

    constructor(other: Quaternion): this(other.x, other.y, other.z, other.w)

    init { normalize() }

    val lengthSquared: Double
        get() = x * x + y * y + z * z + w * w

    val rightVector: Vector
        get() = Vector(1.0 + 2.0 * (-y * y - z * z), 2.0 * (x * y + z * w), 2.0 * (x * z - y * w))

    val upVector: Vector
        get() = Vector(2.0 * (x * y - z * w), 1.0 + 2.0 * (-x * x - z * z), 2.0 * (y * z + x * w))

    val forwardVector: Vector
        get() = Vector(2.0 * (x * z + y * w), 2.0 * (y * z - x * w), 1.0 + 2.0 * (-x * x - y * y))

    override fun clone(): Quaternion {
        return Quaternion(this.x, this.y, this.z, this.w)
    }

    fun conjugate(): Quaternion {
        return Quaternion(this.w, -this.x, -this.y, -this.z)
    } 

    fun toQuaternionF(): Quaternionf {
        return Quaternionf(this.x, this.y, this.z, this.w)
    }

    fun toQuaternionD(): Quaterniond {
        return Quaterniond(this.x, this.y, this.z, this.w)
    }

    /**
     * Divides this quaternion by another quaternion. This operation is equivalent to multiplying
     * with the quaternion after calling [.invert] on it.
     *
     * @param quat to divide with
     */
    fun divide(quat: Quaternion) {
        val x = this.w * -quat.x + (this.x * quat.w) + (this.y * -quat.z) - this.z * -quat.y
        val y = this.w * -quat.y + (this.y * quat.w) + (this.z * -quat.x) - this.x * -quat.z
        val z = this.w * -quat.z + (this.z * quat.w) + (this.x * -quat.y) - this.y * -quat.x
        val w = this.w * quat.w - (this.x * -quat.x) - (this.y * -quat.y) - (this.z * -quat.z)
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        this.normalize()
    }

    /**
     * Multiplies this quaternion with another quaternion. The result is stored in this quaternion.
     *
     * @param Vector to multiply with
     */
    fun multiply(vec: Vector) {
        multiply(Quaternion(vec.x, vec.y, vec.z, 0.0))
    }

    /**
     * Multiplies this quaternion with another quaternion. The result is stored in this quaternion.
     *
     * @param quat to multiply with
     */
    fun multiply(quat: Quaternion) {
        val x = this.w * quat.x + (this.x * quat.w) + (this.y * quat.z) - this.z * quat.y
        val y = this.w * quat.y + (this.y * quat.w) + (this.z * quat.x) - this.x * quat.z
        val z = this.w * quat.z + (this.z * quat.w) + (this.x * quat.y) - this.y * quat.x
        val w = this.w * quat.w - (this.x * quat.x) - (this.y * quat.y) - (this.z * quat.z)
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        this.normalize()
    }

    /**
     * Calculates the dot product of this Quaternion with another
     *
     * @param q other quaternion
     * @return dot product
     */
    fun dot(q: Quaternion): Double {
        return this.x * q.x + (this.y * q.y) + (this.z * q.z) + (this.w * q.w)
    }

    /**
     * Transforms a point, applying the rotation of this quaternion with 0,0,0 as origin.
     *
     * @param vector to rotate using this quaternion
     */
    fun transform(vector: Vector) {
        val px = vector.x
        val py = vector.y
        val pz = vector.z
        vector.setX(px + 2.0 * (px * (-y * y - z * z) + py * (x * y - z * w) + pz * (x * z + y * w)))
        vector.setY(py + 2.0 * (px * (x * y + z * w) + py * (-x * x - z * z) + pz * (y * z - x * w)))
        vector.setZ(pz + 2.0 * (px * (x * z - y * w) + py * (y * z + x * w) + pz * (-x * x - y * y)))
    }

    fun rotate(vector: Vector): Vector {
        transform(vector)
        return vector
    }

    /**
     * Multiplies this Quaternion with a rotation around an axis
     *
     * @param axis vector
     * @param angleDegrees to rotate in degrees
     */
    fun rotateAxis(axis: Vector, angleDegrees: Double) {
        rotateAxis(axis.x, axis.y, axis.z, angleDegrees)
    }

    /**
     * Multiplies this Quaternion with a rotation around an axis
     *
     * @param axisX vector coordinate
     * @param axisY vector coordinate
     * @param axisZ vector coordinate
     * @param angleDegrees to rotate in degrees
     */
    fun rotateAxis(axisX: Double, axisY: Double, axisZ: Double, angleDegrees: Double) {
        this.multiply(fromAxisAngles(axisX, axisY, axisZ, angleDegrees))
    }

    /**
     * Multiplies this quaternion with a rotation transformation in yaw/pitch/roll, based on the Minecraft
     * coordinate system. This will differ slightly from the standard rotateX/Y/Z functions.
     *
     * @param rotation (x=pitch, y=yaw, z=roll)
     */
    fun rotateYawPitchRoll(rotation: Vector) {
        rotateYawPitchRoll(rotation.x, rotation.y, rotation.z)
    }

    /**
     * Multiplies this quaternion with a rotation transformation in yaw/pitch/roll, based on the Minecraft
     * coordinate system. This will differ slightly from the standard rotateX/Y/Z functions.
     *
     * @param yaw rotation (Y)
     * @param pitch rotation (X)
     * @param roll rotation (Z)
     */
    fun rotateYawPitchRoll(yaw: Double, pitch: Double, roll: Double) {
        this.rotateY(-yaw)
        this.rotateX(-pitch)
        this.rotateZ(roll)
    }

    fun getYaw(): Double {
        val test = 2.0 * (w * x - y * z)
        if (abs(test) < (1.0 - 1E-15)) {
            var yaw: Double = atan2(-2.0 * (w * y + z * x), 1.0 - 2.0 * (x * x + y * y))
            val rollX = 0.5 - (x * x + z * z)
            if (rollX <= 0.0 && (abs((w * z + x * y)) > rollX)) {
                yaw += if ((yaw < 0.0)) Math.PI else -Math.PI
            }
            return Math.toDegrees(yaw)
        } else if (test < 0.0) {
            return Math.toDegrees(-2.0 * atan2(z, w))
        } else {
            return Math.toDegrees(2.0 * atan2(z, w))
        }
    }

    fun getPitch(): Double {
        val test = 2.0 * (w * x - y * z)
        if (abs(test) < (1.0 - 1E-15)) {
            var pitch = asin(test)
            val rollX = 0.5 - (x * x + z * z)
            if (rollX <= 0.0 && (abs((w * z + x * y)) > rollX)) {
                pitch = -pitch
                pitch += if ((pitch < 0.0)) Math.PI else -Math.PI
            }
            return Math.toDegrees(-pitch)
        } else if (test < 0.0) {
            return 90.0
        } else {
            return -90.0
        }
    }

    fun getRoll(): Double {
        val test = 2.0 * (w * x - y * z)
        if (abs(test) < (1.0 - 1E-15)) {
            var roll: Double = atan2(2.0 * (w * z + x * y), 1.0 - 2.0 * (x * x + z * z))
            if (abs(roll) > (0.5 * Math.PI)) {
                roll += if ((roll < 0.0)) Math.PI else -Math.PI
            }
            return Math.toDegrees(roll)
        } else {
            return 0.0
        }
    }

    fun getYawPitchRoll(): Vector {
        val test = 2.0 * (w * x - y * z)
        if (abs(test) < (1.0 - 1E-15)) {
            // Standard angle
            var roll: Double = atan2(2.0 * (w * z + x * y), 1.0 - 2.0 * (x * x + z * z))
            var pitch = asin(test)
            var yaw: Double = atan2(-2.0 * (w * y + z * x), 1.0 - 2.0 * (x * x + y * y))

            if (abs(roll) > (0.5 * Math.PI)) {
                roll += if ((roll < 0.0)) Math.PI else -Math.PI
                yaw += if ((yaw < 0.0)) Math.PI else -Math.PI
                pitch = -pitch
                pitch += if ((pitch < 0.0)) Math.PI else -Math.PI
            }

            return Vector(Math.toDegrees(yaw), Math.toDegrees(-pitch), Math.toDegrees(roll))
        } else if (test < 0.0) {
            // This is at the pitch=-90.0 singularity
            // All we can do is yaw (or roll) around the vertical axis
            return Vector(Math.toDegrees(-2.0 * atan2(z, w)), 90.0, 0.0)
        } else {
            // This is at the pitch=90.0 singularity
            // All we can do is yaw (or roll) around the vertical axis
            return Vector(Math.toDegrees(2.0 * atan2(z, w)), -90.0, 0.0)
        }
    }

    /**
     * Rotates the Quaternion 180 degrees around the x-axis
     */
    fun rotateXFlip() {
        val x = this.x
        val y = this.y
        val z = this.z
        val w = this.w
        this.x = w
        this.y = z
        this.z = -y
        this.w = -x
    }

    /**
     * Rotates the Quaternion an angle around the x-axis
     *
     * @param angleDegrees to rotate
     */
    fun rotateX(angleDegrees: Double) {
        if (angleDegrees != 0.0) {
            val r = 0.5 * Math.toRadians(angleDegrees)
            rotateX_unsafe(cos(r), sin(r))
        }
    }

    /**
     * Rotates the Quaternion an angle around the X-axis, the angle defined by the y/z vector.
     * This is equivalent to calling [.rotateX] using [Math.atan2].
     *
     * @param y
     * @param z
     */
    fun rotateX(y: Double, z: Double) {
        val r = halfCosTan2(z, y)
        rotateX_unsafe(sqrt(0.5 + r), sqrt(0.5 - r))
    }

    private fun rotateX_unsafe(fy: Double, fz: Double) {
        val x = this.x * fy + this.w * fz
        val y = this.y * fy + this.z * fz
        val z = this.z * fy - this.y * fz
        val w = this.w * fy - this.x * fz
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        this.normalize()
    }

    /**
     * Rotates the Quaternion 180 degrees around the y-axis
     */
    fun rotateYFlip() {
        val x = this.x
        val y = this.y
        val z = this.z
        val w = this.w
        this.x = -z
        this.y = w
        this.z = x
        this.w = -y
    }

    /**
     * Rotates the Quaternion an angle around the y-axis
     *
     * @param angleDegrees to rotate
     */
    fun rotateY(angleDegrees: Double) {
        if (angleDegrees != 0.0) {
            val r = 0.5 * Math.toRadians(angleDegrees)
            rotateY_unsafe(cos(r), sin(r))
        }
    }

    /**
     * Rotates the Quaternion an angle around the y-axis, the angle defined by the x/z vector.
     * This is equivalent to calling [.rotateY] using [Math.atan2].
     *
     * @param x
     * @param z
     */
    fun rotateY(x: Double, z: Double) {
        val r = halfCosTan2(z, x)
        rotateY_unsafe(sqrt(0.5 + r), sqrt(0.5 - r))
    }

    private fun rotateY_unsafe(fx: Double, fz: Double) {
        val x = this.x * fx - this.z * fz
        val y = this.y * fx + this.w * fz
        val z = this.z * fx + this.x * fz
        val w = this.w * fx - this.y * fz
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        this.normalize()
    }

    /**
     * Rotates the Quaternion 180 degrees around the z-axis
     */
    fun rotateZFlip() {
        val x = this.x
        val y = this.y
        val z = this.z
        val w = this.w
        this.x = y
        this.y = -x
        this.z = w
        this.w = -z
    }

    /**
     * Rotates the Quaternion an angle around the z-axis
     *
     * @param angleDegrees to rotate
     */
    fun rotateZ(angleDegrees: Double) {
        if (angleDegrees != 0.0) {
            val r = 0.5 * Math.toRadians(angleDegrees)
            rotateZ_unsafe(cos(r), sin(r))
        }
    }

    /**
     * Rotates the Quaternion an angle around the z-axis, the angle defined by the x/y vector.
     * This is equivalent to calling [.rotateZ] using [Math.atan2].
     *
     * @param x
     * @param y
     */
    fun rotateZ(x: Double, y: Double) {
        val r = halfCosTan2(y, x)
        rotateZ_unsafe(sqrt(0.5 + r), sqrt(0.5 - r))
    }

    private fun rotateZ_unsafe(fx: Double, fy: Double) {
        val x = this.x * fx + this.y * fy
        val y = this.y * fx - this.x * fy
        val z = this.z * fx + this.w * fy
        val w = this.w * fx - this.z * fy
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        this.normalize()
    }

    /**
     * Inverts this Quaternion.
     */
    fun invert() {
        this.x = -this.x
        this.y = -this.y
        this.z = -this.z
    }

    private fun normalize() {
        val f: Double = lengthSquared.asNormFactor()
        this.x *= f
        this.y *= f
        this.z *= f
        this.w *= f
    }

    companion object {
        /**
         * Creates a quaternion for a rotation around an axis
         *
         * @param axis
         * @param angleDegrees
         * @return quaternion for the rotation around the axis
         */
        fun fromAxisAngles(axis: Vector, angleDegrees: Double): Quaternion {
            return fromAxisAngles(axis.x, axis.y, axis.z, angleDegrees)
        }

        /**
         * Creates a quaternion for a rotation around an axis
         *
         * @param axisX
         * @param axisY
         * @param axisZ
         * @param angleDegrees
         * @return quaternion for the rotation around the axis
         */
        fun fromAxisAngles(axisX: Double, axisY: Double, axisZ: Double, angleDegrees: Double): Quaternion {
            val r = 0.5 * Math.toRadians(angleDegrees)
            val f = sin(r)
            return Quaternion(f * axisX, f * axisY, f * axisZ, cos(r))
        }

        /**
         * Creates a quaternion from yaw/pitch/roll rotations as performed by Minecraft
         *
         * @param rotation (x=pitch, y=yaw, z=roll)
         * @return quaternion for the yaw/pitch/roll rotation
         */
        fun fromYawPitchRoll(rotation: Vector): Quaternion {
            return fromYawPitchRoll(rotation.x, rotation.y, rotation.z)
        }

        /**
         * Creates a quaternion from yaw/pitch/roll rotations as performed by Minecraft
         *
         * @param yaw rotation (Y)
         * @param pitch rotation (X)
         * @param roll rotation (Z)
         * @return quaternion for the yaw/pitch/roll rotation
         */
        fun fromYawPitchRoll(yaw: Double, pitch: Double, roll: Double): Quaternion {
            //TODO: Can be optimized to reduce the number of multiplications
            val quat = Quaternion()
            quat.rotateYawPitchRoll(yaw, pitch, roll)
            return quat
        }

        /**
         * Creates a quaternion that transforms the input vector (u) into the output vector (v).
         * The vectors do not have to be unit vectors for this function to work.
         * The d vector specifies an axis to rotate around when a 180-degree rotation is encountered.
         *
         * @param u input vector (from)
         * @param v expected output vector (to)
         * @param d direction axis around which to rotate for 180-degree angles
         * @return quaternion that rotates u to become v
         */
        fun fromToRotation(u: Vector, v: Vector, d: Vector): Quaternion {
            // xyz = cross(u, v), w = dot(u, v)
            // add magnitude of quaternion to w, then normalize it
            val dot = u.dot(v)
            val q = Quaternion()
            q.x = u.y * v.z - v.y * u.z
            q.y = u.z * v.x - v.z * u.x
            q.z = u.x * v.y - v.x * u.y
            q.w = dot
            q.w += sqrt(q.x * q.x + q.y * q.y + q.z * q.z + q.w * q.w)
            q.normalize()

            // there is a special case for opposite vectors
            // here the quaternion ends up being 0,0,0,0
            // after normalization the terms are NaN as a result (0xinf=NaN)
            if (java.lang.Double.isNaN(q.w)) {
                q.x = d.x
                q.y = d.y
                q.z = d.z
                q.w = 0.0
                q.normalize()
            }
            return q
        }

        /**
         * Creates a quaternion that transforms the input vector (u) into the output vector (v).
         * The vectors do not have to be unit vectors for this function to work.
         *
         * @param u input vector (from)
         * @param v expected output vector (to)
         * @return quaternion that rotates u to become v
         */
//        fun fromToRotation(u: Vector, v: Vector): Quaternion {
//            // xyz = cross(u, v), w = dot(u, v)
//            // add magnitude of quaternion to w, then normalize it
//            val dot = u.dot(v)
//            val q = Quaternion()
//            q.x = u.y * v.z - v.y * u.z
//            q.y = u.z * v.x - v.z * u.x
//            q.z = u.x * v.y - v.x * u.y
//            q.w = dot
//            q.w += sqrt(q.x * q.x + q.y * q.y + q.z * q.z + q.w * q.w)
//            q.normalize()
//
//            // there is a special case for opposite vectors
//            // here the quaternion ends up being 0,0,0,0
//            // after normalization the terms are NaN as a result (0xinf=NaN)
//            if (java.lang.Double.isNaN(q.w)) {
//                if (dot > 0.0) {
//                    // Identity Quaternion
//                    q.setIdentity()
//                } else {
//                    // Rotation of 180 degrees around a certain axis
//                    // First try axis X, then try axis Y
//                    // The cross product with either vector is used for the axis
//                    var norm: Double = MathUtil.getNormalizationFactor(u.z, u.y)
//                    if (java.lang.Double.isInfinite(norm)) {
//                        norm = MathUtil.getNormalizationFactor(u.z, u.x)
//                        q.x = norm * u.z
//                        q.y = 0.0
//                        q.z = norm * -u.x
//                        q.w = 0.0
//                    } else {
//                        q.x = 0.0
//                        q.y = norm * -u.z
//                        q.z = norm * u.y
//                        q.w = 0.0
//                    }
//                }
//            }
//            return q
//        }

        /**
         * Creates a quaternion that transforms a forward vector (0, 0, 1) into the output vector (v).
         * The vector does not have to be a unit vector for this function to work.
         * If the 'up' axis is important, use [.fromLookDirection] instead.
         *
         * @param dir Expected output forward vector (to)
         * @return quaternion that rotates (0,0,1) to become v
         */
        fun fromLookDirection(dir: Vector): Quaternion {
            val q = Quaternion(-dir.y, dir.x, 0.0, dir.z + dir.length())

            // there is a special case when dir is (0, 0, -1)
            if (java.lang.Double.isNaN(q.w)) {
                q.x = 0.0
                q.y = 1.0
                q.z = 0.0
                q.w = 0.0
            }
            return q
        }

        /**
         * Creates a quaternion that 'looks' into a given direction, with a known 'up' vector
         * to dictate roll around that direction axis.
         *
         * @param dir to look into
         * @param up direction
         * @return Quaternion with the look-direction transformation
         */
//        fun fromLookDirection(dir: Vector, up: Vector): Quaternion {
//            // Use the 3x3 rotation matrix solution found on SO, combined with a getRotation()
//            // https://stackoverflow.com/a/18574797
//
//            val D = dir.clone().normalize()
//            val S = up.clone().crossProduct(dir).normalize()
//            val U = D.clone().crossProduct(S)
//            val result: Quaternion = Matrix4x4.fromColumns3x3(S, U, D).getRotation()
//
//            // Fix NaN as a result of dir == up
//            return if (java.lang.Double.isNaN(result.x)) {
//                fromLookDirection(dir)
//            } else {
//                result
//            }
//        }
    }
}