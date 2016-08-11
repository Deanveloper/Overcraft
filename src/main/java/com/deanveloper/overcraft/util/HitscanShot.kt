package com.deanveloper.overcraft.util

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @author Dean
 */
class HitscanShot {
    interface ProjectileShot {
        /**
         * The source of the hitscan
         */
        val source: Entity

        /**
         * What to do in each spot it checks
         */
        fun whileFlying()

        /**
         * What to do if it hits an entity
         */
        fun onHit(e: LivingEntity)

        /**
         * What to do if it hits anything else
         */
        fun onHit()
    }
}