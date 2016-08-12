package com.deanveloper.overcraft.interactive

import com.deanveloper.overcraft.util.Interaction
import org.bukkit.entity.Player

/**
 * @author Dean
 */
abstract class Ability(val useOnEquip: Boolean = false) : Interactive() {
    abstract fun onUse(i: Interaction)

    override fun onClick(e: Interaction) {
        if (!onCooldown(e.player) && !useOnEquip) {
            onUse(e)
        }
    }

    override fun onEquip(p: Player): Boolean {
        if(!onCooldown(p) && useOnEquip) {
            onUse(Interaction(p, this, null, null))
        }

        return useOnEquip
    }

    override fun onUnEquip(p: Player) {}
}