package com.deanveloper.overcraft

import com.deanveloper.overcraft.util.OcPlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Dean
 */
class Overcraft : JavaPlugin() {
    companion object {
        lateinit var instance: Overcraft
            private set
    }

    override fun onEnable() {
        instance = this
    }
}

val Player.oc: OcPlayer
    get() = OcPlayer[this]