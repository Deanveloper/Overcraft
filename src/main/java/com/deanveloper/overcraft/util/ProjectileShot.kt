package com.deanveloper.overcraft.util

import com.deanveloper.kbukkit.runTaskTimer
import com.deanveloper.overcraft.Overcraft
import com.deanveloper.overcraft.PLUGIN
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.scheduler.BukkitTask

/**
 * @param[projectile] The projectile to fire
 * @author Dean
 */
abstract class ProjectileShot(var source: LivingEntity, var projectile: Projectile) : Listener {
    private val task: BukkitTask
    private var ticks: Int = 0

    init {
        task = runTaskTimer(PLUGIN, 1, 2) {
            if (ticks > 20L * 10 || !projectile.isValid) {
                remove()
            } else {
                whileFlying()
                ticks += 2
            }
        }

        Bukkit.getPluginManager().registerEvents(this, PLUGIN)
    }

    @EventHandler
    fun projectileHit(e: ProjectileHitEvent) {
        if(e.entity === projectile) {
            remove()
            onHit(e.entity.location)
        }
    }

    @EventHandler
    fun projectileHit(e: EntityDamageByEntityEvent) {
        if(e.cause === EntityDamageEvent.DamageCause.CUSTOM) return
        if(e.damager === projectile) {
            remove()
            e.isCancelled = true
            if(e.entity is LivingEntity) {

                //if it is genji's reflect hitbox
                if(e.entityType === EntityType.ARMOR_STAND) {
                    val owner = e.entity.getMetadata("reflect").getOrNull(0) as Player?
                    if(owner != null) {
                        source = owner
                        ticks = 0
                        projectile.velocity = source.location.direction.normalize()
                                .multiply(projectile.velocity.length())
                        projectile.shooter = owner

                        source.world.playSound(source.location, Sound.BLOCK_ANVIL_PLACE, 1f, 1f)
                        return
                    }
                }

                onHit(e.entity as LivingEntity)
            }
        }
    }

    fun remove() {
        task.cancel()
        if(projectile.isValid) {
            projectile.remove()
        }

        HandlerList.unregisterAll(this)
    }

    /**
     * What to do while it's flying
     */
    abstract fun whileFlying()

    /**
     * What to do if it hits an entity
     */
    abstract fun onHit(hit: LivingEntity)

    /**
     * What to do if it hits anything else
     */
    abstract fun onHit(loc: Location)
}