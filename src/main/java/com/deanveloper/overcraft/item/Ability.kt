package com.deanveloper.overcraft.item

import com.deanveloper.overcraft.util.Interaction
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Colorable
import org.bukkit.material.Dye
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author Dean
 */
abstract class Ability(val useOnEquip: Boolean = true) : Interactive() {
    abstract fun onUse(i: Interaction)

    override fun onClick(e: Interaction) {
        if (!cooldowns[e.player] && !useOnEquip) {
            onUse(e)
        }
    }

    override fun onEquip(p: Player): Boolean {
        if(!cooldowns[p] && useOnEquip) {
            onUse(Interaction(p, this, null, null))
        }

        return useOnEquip
    }

    override fun onUnEquip(p: Player) = false
}