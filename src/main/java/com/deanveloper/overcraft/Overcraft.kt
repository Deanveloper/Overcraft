package com.deanveloper.overcraft

import com.deanveloper.overcraft.util.OcPlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Dean
 */
private var _PLUGIN: Overcraft? = null
val PLUGIN: Overcraft
    get() = _PLUGIN!!
class Overcraft : JavaPlugin() {
    override fun onEnable() {
        _PLUGIN = this
    }
}

val Player.oc: OcPlayer
    get() = OcPlayer[this]