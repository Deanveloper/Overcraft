package com.deanveloper.overcraft.util

import com.deanveloper.overcraft.item.Interactive
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action

/**
 * @author Dean
 */
class Interaction(
        val player: Player,
        val item: Interactive,
        val target: LivingEntity? = null,
        val click: Click? = Interaction.Click.LEFT
) {
    enum class Click {
        LEFT,
        RIGHT
    }
}

val Action.toClick: Interaction.Click?
    get() = when(this) {
        Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR -> Interaction.Click.LEFT
        Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR -> Interaction.Click.RIGHT
        Action.PHYSICAL -> null
    }