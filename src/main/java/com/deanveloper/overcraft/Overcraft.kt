package com.deanveloper.overcraft

import com.deanveloper.overcraft.util.OcPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
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
        Bukkit.getPluginManager().registerEvents(GeneralListener, this)
    }
}

val Player.oc: OcPlayer
    get() = OcPlayer[this]

object GeneralListener : Listener {
    @EventHandler
    fun onDeath(d: PlayerDeathEvent) {
        d.deathMessage = "${d.entity.displayName} was killed"

        val lastAttacker = OcPlayer[d.entity].lastAttacker
        if(lastAttacker !== null) {
            d.deathMessage += " by ${lastAttacker.displayName}"
        }

        d.keepInventory = true
        d.keepLevel = true
        d.drops.clear()
    }
}