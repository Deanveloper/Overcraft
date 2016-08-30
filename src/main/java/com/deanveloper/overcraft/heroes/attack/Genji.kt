package com.deanveloper.overcraft.heroes.attack

import com.deanveloper.overcraft.heroes.HeroBase
import com.deanveloper.overcraft.interactive.offense.Dragonblade
import com.deanveloper.overcraft.interactive.offense.Reflect
import com.deanveloper.overcraft.interactive.offense.Shuriken
import com.deanveloper.overcraft.interactive.offense.SwiftStrike
import com.deanveloper.overcraft.util.OcPlayer
import org.bukkit.Bukkit

/**
 * @author Dean
 */
object Genji : HeroBase {
    override val items = listOf(Shuriken, Reflect, SwiftStrike, Dragonblade)
    override fun onSpawn(p: OcPlayer) {
        p.walkSpeed = .25f
    }
}