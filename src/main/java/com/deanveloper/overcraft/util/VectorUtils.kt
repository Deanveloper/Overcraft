@file:Suppress("NAME_SHADOWING")

package com.deanveloper.overcraft.util

import org.bukkit.Location
import org.bukkit.util.Vector


fun Vector.rotateAroundX(angle: Double): Vector {
    val angle = Math.toRadians(angle)
    val cos = Math.cos(angle)
    val sin = Math.sin(angle)
    val y = this.y * cos - this.z * sin
    val z = this.y * sin + this.z * cos
    return setY(y).setZ(z)
}

fun Vector.rotateAroundY(angle: Double): Vector {
    val angle = Math.toRadians(angle)
    val cos = Math.cos(angle)
    val sin = Math.sin(angle)
    val x = this.x * cos + this.z * sin
    val z = this.x * -sin + this.z * cos
    return setX(x).setZ(z)
}

fun Vector.rotateAroundZ(angle: Double): Vector {
    val angle = Math.toRadians(angle)
    val cos = Math.cos(angle)
    val sin = Math.sin(angle)
    val x = this.x * cos - this.y * sin
    val y = this.x * sin + this.y * cos
    return setX(x).setY(y)
}

fun Vector.rotate(angleX: Double, angleY: Double, angleZ: Double): Vector {
    val angleX = Math.toRadians(angleX)
    val angleY = Math.toRadians(angleY)
    val angleZ = Math.toRadians(angleZ)
    rotateAroundX(angleX)
    rotateAroundY(angleY)
    rotateAroundZ(angleZ)
    return this
}

/**
 * Rotate a vector about a location using that location's direction
 */
fun Vector.rotate(location: Location): Vector {
    return this.rotate(location.yaw, location.pitch)
}

/**
 * This handles non-unit vectors, with yaw and pitch instead of X,Y,Z angles.

 * Thanks to SexyToad!
 */
fun Vector.rotate(yawDegrees: Float, pitchDegrees: Float): Vector {
    val yaw = Math.toRadians((-1 * (yawDegrees + 90)).toDouble())
    val pitch = Math.toRadians((-pitchDegrees).toDouble())

    val cosYaw = Math.cos(yaw)
    val cosPitch = Math.cos(pitch)
    val sinYaw = Math.sin(yaw)
    val sinPitch = Math.sin(pitch)

    var initialX: Double
    val initialY: Double
    val initialZ: Double
    var x: Double
    val y: Double
    val z: Double

    // Z_Axis rotation (Pitch)
    initialX = this.x
    initialY = this.y
    x = initialX * cosPitch - initialY * sinPitch
    y = initialX * sinPitch + initialY * cosPitch

    // Y_Axis rotation (Yaw)
    initialZ = this.z
    initialX = x
    z = initialZ * cosYaw - initialX * sinYaw
    x = initialZ * sinYaw + initialX * cosYaw

    return Vector(x, y, z)
}

fun Vector.angleToXAxis(): Double {
    return Math.atan2(x, y)
}