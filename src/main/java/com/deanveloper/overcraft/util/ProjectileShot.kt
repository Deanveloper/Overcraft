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
import org.bukkit.util.BlockIterator

/**
 * @author Dean
 */
abstract class ProjectileShot : Listener {
    private val task: BukkitTask

    init {
        if(projectile is Projectile) {
            task = runTaskTimer(Overcraft.instance, 1, 2) {
                if(!projectile.isValid) {
                    this.cancel()
                    onHit()
                } else {
                    whileFlying()
                }
            }
        } else {
            task = runTaskTimer(Overcraft.instance, 1, 2) {
                val nextBlock = BlockIterator(projectile.location, 0.0, 1).next()
                if(nextBlock.typeId != 0 || !projectile.isValid) {
                    this.cancel()
                    onHit()
                } else {
                    whileFlying()
                }
            }
        }

        Bukkit.getPluginManager().registerEvents(this, Overcraft.instance)
    }

    @EventHandler
    fun onHit(e: ProjectileHitEvent) {
        if(e.entity == projectile) {
            task.cancel()
        }
    }

    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        if(e.damager == projectile) {
            task.cancel()
            if(e.entity is LivingEntity) {
                onHit(e.entity as LivingEntity)
            }
        }
    }

    /**
     * The source of the projectile
     */
    abstract val source: Entity

    /**
     * Our projectile
     */
    abstract val projectile: Entity

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