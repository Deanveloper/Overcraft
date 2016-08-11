package com.deanveloper.overcraft.util

import com.deanveloper.overcraft.interactive.Interactive
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action

/**
 * @author Dean
 */
class AbilityUse(
        val player: Player,
        val item: Interactive,
        val target: LivingEntity? = null,
        val click: Click = AbilityUse.Click.LEFT
) {
    enum class Click {
        LEFT,
        RIGHT
    }
}

val Action.toClick: AbilityUse.Click?
    get() = when(this) {
        Action.LEFT_CLICK_BLOCK, Action.LEFT_CLICK_AIR -> AbilityUse.Click.LEFT
        Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR -> AbilityUse.Click.RIGHT
        Action.PHYSICAL -> null
    }