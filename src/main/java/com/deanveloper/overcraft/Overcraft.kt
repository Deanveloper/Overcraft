package com.deanveloper.overcraft

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