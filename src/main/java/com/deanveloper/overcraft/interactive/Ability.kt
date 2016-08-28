package com.deanveloper.overcraft.interactive

import com.deanveloper.overcraft.util.Interaction
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.material.Colorable
import java.util.*

/**
 * @author Dean
 */
abstract class Ability(val useOnEquip: Boolean = true) : Interactive() {
    override val type = Material.STAINED_GLASS_PANE

    init {
        (item.data as Colorable).color = DyeColor.LIME
    }

    abstract val cooldown: Long
    abstract fun onUse(i: Interaction)

    fun startCooldown(id: UUID) {
        (item.data as Colorable).color = DyeColor.GRAY
        cooldowns.addCooldown(id, cooldown) {
            (item.data as Colorable).color = DyeColor.LIME
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