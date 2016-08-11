package com.deanveloper.overcraft.interactive.offense

import com.deanveloper.kbukkit.plus
import com.deanveloper.overcraft.interactive.Weapon
import com.deanveloper.overcraft.util.AbilityUse
import org.bukkit.ChatColor
import org.bukkit.Material

/**
 * @author Dean
 */
object Shuriken : Weapon() {
    override val itemType = Material.NETHER_STAR
    override val name = ChatColor.GREEN + "Shuriken"
    override val lore = listOf(
            "Left Click",
            " - Shoot three shurikens in a row",
            "",
            "Right Click",
            " - Shoot three shurikens in a fan pattern"
    )

    override fun onClick(e: AbilityUse) {
        if(e.click == AbilityUse.Click.LEFT) {

        }
    }
}