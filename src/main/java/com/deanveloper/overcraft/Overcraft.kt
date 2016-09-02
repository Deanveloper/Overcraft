package com.deanveloper.overcraft

import com.deanveloper.overcraft.commands.HeroCommand
import com.deanveloper.overcraft.util.OcPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Dean
 */
private var _PLUGIN: Overcraft? = null
val PLUGIN: Overcraft
    get() = _PLUGIN!!

class Overcraft : JavaPlugin() {
    override fun onEnable() {
        _PLUGIN = this
        Bukkit.getPluginManager().registerEvents(GeneralListener, this)
        getCommand("hero").executor = HeroCommand
        getCommand("hero").tabCompleter = HeroCommand
    }
}

val Player.oc: OcPlayer
    get() = OcPlayer[this]

object GeneralListener : Listener {
    @EventHandler
    fun onDeath(d: PlayerDeathEvent) {
        d.deathMessage = "${d.entity.displayName} was killed"

        val p = OcPlayer[d.entity]
        val lastAttacker = p.lastAttacker
        if (lastAttacker !== null) {
            d.deathMessage += " by ${lastAttacker.displayName}"
        }

        d.keepInventory = true
        d.keepLevel = true
        d.drops.clear()

        p.onDeath()
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun cancelNonCustom(e: EntityDamageByEntityEvent) {
        val ent = e.entity
        val damager = e.damager

        // if a player has a hero selected, make sure that their hits don't register
        // unless the plugin does it for them
        if (damager is Projectile) {
            val shooter = damager.shooter
            // if a hero is selected
            if (shooter is Player) {
                if (shooter.oc.hero !== null) {
                    if(e.cause !== EntityDamageEvent.DamageCause.CUSTOM) {
                        e.isCancelled = true
                    }
                }
            }
        } else if (damager.type === EntityType.PLAYER) {
            damager as Player
            // if a hero is selected
            if (damager.oc.hero !== null) {
                // refuse damage dealt by them if it is not magic
                if(e.cause !== EntityDamageEvent.DamageCause.CUSTOM) {
                    e.isCancelled = true
                }
            }
        }
        if (ent.type.isAlive) {
            ent as LivingEntity
            ent.noDamageTicks = 0
            ent.maximumNoDamageTicks = 0
        }
    }

    fun noItemDamage(e: PlayerItemDamageEvent) {
        if(e.player?.oc?.hero === null) {
            e.isCancelled = true
        }
    }
}


fun LivingEntity.hurt(damage: Double, from: Entity) {
    if (this.type === EntityType.PLAYER) {
        this as Player // smart cast
        if (from is Projectile && from.shooter is Player) {
            from as Player // smart cast
            this.oc.lastAttacker = from.oc
        } else if (from.type === EntityType.PLAYER) {
            from as Player // smart cast
            this.oc.lastAttacker = from.oc
        }
    }
    this.damage(damage)
    this.maximumNoDamageTicks = 0
    this.noDamageTicks = 0
}