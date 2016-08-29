package com.deanveloper.overcraft.interactive

import com.deanveloper.overcraft.util.Interaction
import org.bukkit.entity.Player

/**
 * @author Dean
 */
abstract class Ultimate(val useOnEquip: Boolean = true) : Interactive() {
    /**
     * Whether they will be allowed to use other
     * weapons while the ultimate is being used
     */
    abstract val honorBound: Boolean

    var percent: Double = 0.0
    var beingUsed: Boolean = false

    abstract fun onUse(i: Interaction)

    open fun onAttack(i: Interaction) {}

    override fun onClick(e: Interaction) {
        if (percent >= 1.0 && !useOnEquip) {
            onUse(e)
        } else if(beingUsed && !cooldowns[e.player]) {
            onAttack(e)
        }
    }

    override fun onEquip(p: Player): Boolean {
        if(percent >= 1.0 && useOnEquip) {
            onUse(Interaction(p, this, null, null))
        }

        return useOnEquip
    }

    override fun onUnEquip(p: Player): Boolean {
        return honorBound && beingUsed
    }
}