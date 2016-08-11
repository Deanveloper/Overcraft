package com.deanveloper.overcraft.util

import com.deanveloper.kbukkit.runTaskTimer
import com.deanveloper.overcraft.Overcraft
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.scheduler.BukkitTask

/**
 * @param[projectile] The projectile to fire
 * @author Dean
 */
abstract class ProjectileShot(val source: Entity, val projectile: Projectile) : Listener {
    private val task: BukkitTask
    private val ticks: Int = 0

    init {
        task = runTaskTimer(Overcraft.instance, 1, 2) {
            if (ticks > 20L * 10 || !projectile.isValid) {
                if(projectile.isValid) {
                    projectile.remove()
                }
                this.cancel()
            } else {
                whileFlying()
            }
        }

        Bukkit.getPluginManager().registerEvents(this, Overcraft.instance)
    }

    @EventHandler
    fun projectileHit(e: ProjectileHitEvent) {
        if(e.entity == projectile) {
            task.cancel()
            onHit()
        }
    }

    @EventHandler
    fun projectileHit(e: EntityDamageByEntityEvent) {
        if(e.damager == projectile) {
            task.cancel()
            e.isCancelled = true
            if(e.entity is LivingEntity) {
                onHit(e.entity as LivingEntity)
            }
        }
    }

    /**
     * What to do while it's flying
     */
    abstract fun whileFlying()

    /**
     * What to do if it hits an entity
     */
    abstract fun onHit(e: LivingEntity)

    /**
     * What to do if it hits anything else
     */
    abstract fun onHit()
}