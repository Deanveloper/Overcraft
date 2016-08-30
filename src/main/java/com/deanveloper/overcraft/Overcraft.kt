package com.deanveloper.overcraft

import com.deanveloper.overcraft.commands.HeroCommand
import com.deanveloper.overcraft.util.OcPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
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
        if(lastAttacker !== null) {
            d.deathMessage += " by ${lastAttacker.displayName}"
        }

        d.keepInventory = true
        d.keepLevel = true
        d.drops.clear()

        p.onDeath()
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun cancelNonCustom(e: EntityDamageEvent) {
        if(e.cause !== EntityDamageEvent.DamageCause.CUSTOM && e.cause !== EntityDamageEvent.DamageCause.VOID) {
            e.isCancelled = true
        }
        val ent = e.entity
        if(ent.type.isAlive) {
            ent as LivingEntity
            ent.noDamageTicks = 0
            ent.maximumNoDamageTicks = 0
        }
    }
}


fun LivingEntity.hurt(damage: Double, from: LivingEntity) {
    if(this.type === EntityType.PLAYER && from.type === EntityType.PLAYER) {
        this as Player // smart cast
        from as Player // smart cast
        this.oc.lastAttacker = from.oc
    }
    this.damage(damage)
    this.maximumNoDamageTicks = 0
    this.noDamageTicks = 0
}