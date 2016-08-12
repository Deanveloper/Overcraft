package com.deanveloper.overcraft.util

import com.deanveloper.kbukkit.CustomPlayer
import com.deanveloper.kbukkit.CustomPlayerCompanion
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Dean
 */
class OcPlayer private constructor(p: Player) : CustomPlayer(p), Player by p {
    companion object : CustomPlayerCompanion<OcPlayer>(::OcPlayer)

    val removeOnDeath: MutableSet<Entity> = Collections.newSetFromMap(WeakHashMap<Entity, Boolean>())

    fun onDeath() {
        removeOnDeath.filter { it.isValid }.forEach { it.remove() }
        removeOnDeath.clear()
    }
}