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
    companion object : CustomPlayerCompanion<OcPlayer>({ OcPlayer(it) })

    val removeOnDeath: MutableSet<Entity> = Collections.newSetFromMap(WeakHashMap<Entity, Boolean>())
    var isGenjiReflecting = false
    var hero: HeroBase? = null
        set(newHero) {
            player.inventory.clear()
            player.walkSpeed = .2f
            if (newHero != null) {
                newHero.items.forEachIndexed { index, item ->
                    player.inventory.setItem(item.slot, item.items.main)
                }
                newHero.onSpawn(this)
            }

            field = newHero
        }

    var lastAttacker: OcPlayer? = null

    fun onDeath() {
        removeOnDeath.filter { it.isValid }.forEach { it.remove() }
        removeOnDeath.clear()
    }
}