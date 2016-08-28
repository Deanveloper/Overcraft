package com.deanveloper.overcraft.interactive

import com.deanveloper.overcraft.util.Interaction
import org.bukkit.entity.Player

/**
 * @author Dean
 */
abstract class Weapon : Interactive() {
    abstract fun onUse(e: Interaction)

    override final fun onClick(e: Interaction) {
        if (!cooldowns[e.player]) {
            onUse(e)
        }
    }
    override fun onEquip(p: Player) = false
    override fun onUnEquip(p: Player) {}
}