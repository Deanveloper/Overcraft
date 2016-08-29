package com.deanveloper.overcraft.interactive

import com.deanveloper.overcraft.util.Interaction
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.material.Colorable
import org.bukkit.material.Dye
import java.util.*

/**
 * @author Dean
 */
abstract class Ability(val useOnEquip: Boolean = true) : Interactive() {
    override val type = Material.STAINED_GLASS_PANE

    init {
        if(type === Material.STAINED_GLASS_PANE) {
            item.data.data = DyeColor.LIME.woolData
        }
    }

    abstract val cooldown: Long
    abstract fun onUse(i: Interaction)

    fun startCooldown(id: UUID) {
        item.type = Material.STAINED_GLASS_PANE
        item.data.data = DyeColor.GRAY.woolData

        cooldowns.addCooldown(id, cooldown) {
            item.type = type
            if(type === Material.STAINED_GLASS_PANE) {
                item.data.data = DyeColor.LIME.woolData
            }
        }
    }

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