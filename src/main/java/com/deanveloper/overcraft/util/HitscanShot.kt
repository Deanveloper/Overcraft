package com.deanveloper.overcraft.util

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * @author Dean
 */
abstract class HitscanShot(
        var source: Entity,
        var loc: Location = source.location,
        _vec: Vector = source.location.direction
) {
    var vec = _vec.normalize().multiply(.2)!!

    init {
        val entities = source.world.livingEntities.apply { remove(source) }

        dance@for (i in 0..1000) {
            loc.add(vec)

            val hit = entities.filter {
                it.location.distanceSquared(loc) < 1 || it.eyeLocation.distanceSquared(loc) < 1
            }

            for(e in hit) {
                //if it is genji's reflect hitbox
                if(e.type === EntityType.ARMOR_STAND) {
                    val owner = e.getMetadata("reflect").getOrNull(0) as Player?
                    if(owner != null) {
                        source = owner
                        loc = owner.location
                        vec = owner.location.direction
                        source.world.playSound(source.location, Sound.BLOCK_ANVIL_PLACE, 1f, 1f)
                    }
                }
                if(!onHit(e)) break@dance
            }

            if(loc.block !== null && loc.block.typeId !== 0) {
                if(!onHit(loc)) break@dance
            }

            if(!whileFlying(loc)) break@dance
        }
    }

    /**
     * What to do in each spot it checks
     *
     * @return whether to continue the hitscan
     */
    abstract fun whileFlying(loc: Location): Boolean

    /**
     * What to do if it hits an entity
     *
     * @return whether to continue the hitscan
     */
    abstract fun onHit(e: LivingEntity): Boolean

    /**
     * What to do if it hits anything else
     *
     * @return whether to continue the hitscan
     */
    abstract fun onHit(loc: Location): Boolean
}