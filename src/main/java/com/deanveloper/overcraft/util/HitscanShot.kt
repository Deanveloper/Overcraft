package com.deanveloper.overcraft.util

import com.deanveloper.overcraft.oc
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*

/**
 * @author Dean
 */
abstract class HitscanShot(
        var source: LivingEntity,
        var loc: Location = source.eyeLocation,
        _vec: Vector = source.location.direction,
        val isReflectable: Boolean = true
) {
    var vec = _vec.normalize().multiply(.2)!!
    private val alreadyHit = mutableSetOf<UUID>()

    init {
        val entities = source.world.livingEntities.apply { remove(source) }

        dance@for (i in 0..1000) {
            loc.add(vec)

            val hit = entities
                    .filterNot { it.location.distanceSquared(loc) > 1 }
                    .filterNot { it.eyeLocation.distanceSquared(loc) > 1 }
                    .filterNot { it.uniqueId in alreadyHit }

            for (ent in hit) {
                alreadyHit.add(ent.uniqueId)
                //if it is genji's reflect
                if (isReflectable && ent.type === EntityType.PLAYER) {
                    ent as Player // smart cast
                    //multiply by 100 because hitscan has high speed
                    if (ent.oc.shouldReflect(vec.clone().multiply(100))) {
                        source = ent
                        loc = ent.location
                        vec = ent.location.direction
                        source.world.playSound(source.location, Sound.BLOCK_ANVIL_PLACE, 1f, 1f)
                    }
                }
                if (!onHit(ent)) break@dance
            }

            if (loc.block !== null && loc.block.type.isSolid) {
                if (!onHit(loc)) break@dance
            }

            if (!whileFlying(loc)) break@dance
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