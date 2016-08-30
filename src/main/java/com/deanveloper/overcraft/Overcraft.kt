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
            Bukkit.broadcastMessage("Projectile")
            val shooter = damager.shooter
            // if a hero is selected
            if (shooter is Player) {
                Bukkit.broadcastMessage("Player")
                if (shooter.oc.hero !== null) {
                    Bukkit.broadcastMessage("Hero")
                    if(e.cause !== EntityDamageEvent.DamageCause.CUSTOM) {
                        Bukkit.broadcastMessage("Custom")
                        e.isCancelled = true
                    }
                }
            }
        } else if (damager.type === EntityType.PLAYER) {
            Bukkit.broadcastMessage("Player")
            damager as Player
            // if a hero is selected
            if (damager.oc.hero !== null) {
                Bukkit.broadcastMessage("Hero")
                // refuse damage dealt by them if it is not magic
                if(e.cause !== EntityDamageEvent.DamageCause.CUSTOM) {
                    Bukkit.broadcastMessage("Custom")
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun monitorDamage(e: EntityDamageByEntityEvent) {
        val damager = e.damager
        if(damager is Projectile) {
            Bukkit.broadcastMessage("${e.cause}: ${e.damage} (${damager.shooter}")
        } else {
            Bukkit.broadcastMessage("${e.cause}: ${e.damage}")
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