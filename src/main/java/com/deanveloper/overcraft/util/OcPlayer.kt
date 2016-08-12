package com.deanveloper.overcraft.util

import com.deanveloper.kbukkit.CustomPlayer
import com.deanveloper.kbukkit.CustomPlayerCompanion
import org.bukkit.entity.Player

/**
 * @author Dean
 */
class OcPlayer private constructor(p: Player) : CustomPlayer(p), Player by p {
    companion object : CustomPlayerCompanion<OcPlayer>(::OcPlayer)
}

val Player.oc: OcPlayer
    get() = OcPlayer[this]