package com.deanveloper.overcraft.commands

import com.deanveloper.overcraft.heroes.HeroBase
import com.deanveloper.overcraft.heroes.Heroes
import com.deanveloper.overcraft.util.OcPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Dean
 */
object HeroCommand : CommandExecutor, TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val hero: HeroBase? = Heroes.values().firstOrNull { it.name == args.getOrNull(0)?.toUpperCase() }?.hero
            val p = OcPlayer[sender]

            if(hero === null) {
                sender.sendMessage("Available heroes: ${Arrays.toString(Heroes.values())}")
            } else {
                p.hero = hero
            }

            return true

        } else {
            sender.sendMessage("You need to be a player to do that!")
            return true
        }
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command, lbl: String, args: Array<out String>): List<String>? {
        if(args.size == 1) {
            val soFar = args[0]
            return Heroes.values().filter { it.name.startsWith(soFar.toUpperCase()) }.map { it.toString() }
        }
        return emptyList()
    }
}