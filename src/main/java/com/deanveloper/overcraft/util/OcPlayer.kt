package com.deanveloper.overcraft.util

import com.deanveloper.kbukkit.CustomPlayer
import com.deanveloper.kbukkit.CustomPlayerCompanion
import com.deanveloper.overcraft.heroes.HeroBase
import com.deanveloper.overcraft.oc
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Dean
 */
class OcPlayer private constructor(p: Player) : CustomPlayer(p), Player by p {
    companion object : CustomPlayerCompanion<OcPlayer>(::OcPlayer)
    val removeOnDeath: MutableSet<Entity> = Collections.newSetFromMap(WeakHashMap<Entity, Boolean>())
    var hero: HeroBase? = null
        set(newHero) {
            player.inventory.clear()
            player.walkSpeed = .2f
            if(newHero != null) {
                newHero.items.forEachIndexed { index, item ->
                    player.inventory.setItem(item.slot, item.items.main)
                }
                newHero.onSpawn(this)
            }
        }

    fun damageOther(ent: LivingEntity, damage: Double) {
        if(ent.type === EntityType.PLAYER) {
            ent as Player // smart cast
            ent.oc.lastAttacker = this
        }
        ent.maximumNoDamageTicks = 0
        ent.damage(damage)
    }

    var lastAttacker: OcPlayer? = null

    fun onDeath() {
        removeOnDeath.filter { it.isValid }.forEach { it.remove() }
        removeOnDeath.clear()
    }
}