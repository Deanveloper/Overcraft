package com.deanveloper.overcraft.util

import com.deanveloper.kbukkit.runTask
import com.deanveloper.kbukkit.runTaskTimer
import com.deanveloper.overcraft.PLUGIN
import com.deanveloper.overcraft.oc
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.scheduler.BukkitTask
import java.util.*

/**
 * @param[projectile] The projectile to fire
 * @author Dean
 */
abstract class ProjectileShot(var source: LivingEntity, var projectile: Projectile) : Listener {
    private val task: BukkitTask
    private var ticks: Int = 0

    companion object {
        @JvmStatic val trackedProjectiles = mutableSetOf<UUID>()
    }

    init {
        task = runTaskTimer(PLUGIN, 1, 2) {
            if (ticks > 20L * 10 || !projectile.isValid) {
                remove()
            } else {
                whileFlying()
                ticks += 2
            }
        }

        trackedProjectiles.add(projectile.uniqueId)
        Bukkit.getPluginManager().registerEvents(this, PLUGIN)
    }

    @EventHandler
    fun projectileHit(e: ProjectileHitEvent) {
        if (e.entity === projectile) {
            onHit(e.entity.location)
            runTask(PLUGIN) {
                remove() // call after EntityDamageByEntity
            }
        }
    }

    @EventHandler
    fun projectileHit(e: EntityDamageByEntityEvent) {
        if (e.cause === EntityDamageEvent.DamageCause.CUSTOM) return
        if (e.damager === projectile) {
            remove()
            val hit = e.entity
            if (hit is Player && hit.oc.isGenjiReflecting) {
                projectile = projectile.world.spawn(projectile.location, projectile.javaClass)
                projectile.shooter = hit
                source = hit
                ticks = 0
                projectile.velocity = source.location.direction.normalize()
                        .multiply(projectile.velocity.length())
                projectile.shooter = hit

                source.world.playSound(source.location, Sound.BLOCK_ANVIL_PLACE, 1f, 1f)
                return
            }
            if(hit.type.isAlive) {
                onHit(hit as LivingEntity)
            }
        }
    }

    fun remove() {
        task.cancel()
        if (projectile.isValid) {
            projectile.remove()
        }
        trackedProjectiles.remove(projectile.uniqueId)

        HandlerList.unregisterAll(this)
    }

    /**
     * What to do while it's flying
     */
    abstract fun whileFlying()

    /**
     * What to do if it hits an entity
     */
    abstract fun onHit(target: LivingEntity)

    /**
     * What to do if it hits anything else
     */
    abstract fun onHit(loc: Location)
}