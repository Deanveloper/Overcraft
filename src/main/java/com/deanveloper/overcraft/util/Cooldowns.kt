package com.deanveloper.overcraft.util

import com.deanveloper.kbukkit.runTaskLater
import com.deanveloper.overcraft.Overcraft
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

/**
 * @author Dean
 */
class Cooldowns {
    private val cooldowns = mutableMapOf<UUID, Pair<BukkitTask, () -> Unit>>()

    companion object {
        val DO_NOTHING: () -> Unit = {}
    }

    /**
     * Add a cooldown
     *
     * @param p         The player to add the cooldown for
     * @param ticks     The length of the cooldown in ticks
     * @param onRemove  What to do when the cooldown expires
     */
    fun addCooldown(p: Player, ticks: Long, onRemove: () -> Unit = DO_NOTHING) = addCooldown(p.uniqueId, ticks, onRemove)

    fun addCooldown(id: UUID, ticks: Long, onRemove: () -> Unit = DO_NOTHING) {
        cooldowns[id] = Pair(runTaskLater(Overcraft.instance, ticks, { remove(id) }), onRemove)
    }

    operator fun get(id: UUID) = id in cooldowns

    operator fun get(p: Player) = get(p.uniqueId)

    fun isOnCooldown(id: UUID) = get(id)

    fun isOnCooldown(p: Player) = get(p.uniqueId)

    fun remove(p: Player) = remove(p.uniqueId)

    fun remove(id: UUID) {
        val (task, onRemove) = cooldowns[id] ?: return
        task.cancel()
        onRemove()
    }
}