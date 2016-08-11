package com.deanveloper.overcraft.util

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

/**
 * @author Dean
 */
abstract class HitscanShot(
        val source: Entity,
        val loc: Location = source.location,
        _vec: Vector = source.location.direction
) {
    val vec = _vec.normalize().multiply(.2)!!

    init {
        val entities = source.world.livingEntities.apply { remove(source) }

        dance@for (i in 0..500) {
            loc.add(vec)

            val hit = entities.filter {
                it.location.distanceSquared(loc) < 1 || it.eyeLocation.distanceSquared(loc) < 1
            }

            for(e in hit) {
                if(onHit(e)) break@dance
            }
            if(whileFlying(loc)) break@dance
        }
    }

    /**
     * What to do in each spot it checks
     *
     * @return whether to stop the hitscan
     */
    abstract fun whileFlying(loc: Location): Boolean

    /**
     * What to do if it hits an entity
     *
     * @return whether to stop the hitscan
     */
    abstract fun onHit(e: LivingEntity): Boolean

    /**
     * What to do if it hits anything else
     */
    abstract fun onHit()
}