package com.deanveloper.overcraft.interactive

import com.deanveloper.overcraft.util.Interaction
import org.bukkit.entity.Player

/**
 * @author Dean
 */
abstract class Weapon : Interactive() {
    override val cooldownItem = item
    override val slot = 0

    abstract fun onUse(e: Interaction)

    override final fun onClick(e: Interaction) {
        if (!cooldowns[e.player]) {
            onUse(e)
        }
    }

    /**
     * When the interactive is equipped
     *
     * @return whether to keep the cursor on the item
     */
    override fun onEquip(p: Player) = false

    /**
     * When the interactive is unequipped
     *
     * @return whether to move the cursor back to the main weapon
     */
    override fun onUnEquip(p: Player): Boolean = false
}