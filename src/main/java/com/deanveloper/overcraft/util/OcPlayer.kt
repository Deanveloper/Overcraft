package com.deanveloper.overcraft.util

import com.deanveloper.kbukkit.CustomPlayer
import com.deanveloper.kbukkit.CustomPlayerCompanion
import com.deanveloper.overcraft.heroes.HeroBase
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Dean
 */
class OcPlayer private constructor(p: Player) : CustomPlayer(p), Player by p {
    companion object : CustomPlayerCompanion<OcPlayer>(::OcPlayer)
    val removeOnDeath: MutableSet<Entity> = Collections.newSetFromMap(WeakHashMap<Entity, Boolean>())
    var hero: HeroBase? = null
        set(value) {
            player.inventory.clear()
            player.walkSpeed = .2f
            if(value != null) {
                player.inventory.addItem(*value.items.map { it?.item }.toTypedArray())
                value.onSpawn(this)
            }
        }

    var lastAttacker: OcPlayer? = null

    fun onDeath() {
        removeOnDeath.filter { it.isValid }.forEach { it.remove() }
        removeOnDeath.clear()
    }
}