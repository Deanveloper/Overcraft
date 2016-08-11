package com.deanveloper.overcraft.interactive.offense

import com.deanveloper.kbukkit.plus
import com.deanveloper.kbukkit.runTaskLater
import com.deanveloper.overcraft.Overcraft
import com.deanveloper.overcraft.interactive.Weapon
import com.deanveloper.overcraft.util.AbilityUse
import com.deanveloper.overcraft.util.ProjectileShot
import com.deanveloper.overcraft.util.rotateAroundY
import org.bukkit.ChatColor
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.entity.LivingEntity

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
        if (onCooldown(e.player)) return

        if (e.click == AbilityUse.Click.LEFT) {
            for (i in 0L..11L step 4L) {
                runTaskLater(Overcraft.instance, i) {
                    val arrow = e.player.world.spawnArrow(e.player.eyeLocation, e.player.eyeLocation.direction, 0.6f, 0f)
                    arrow.setGravity(false)
                    object : ProjectileShot(e.player, arrow) {
                        override fun whileFlying() {
                            projectile.world.spigot().playEffect(projectile.location, Effect.MAGIC_CRIT, 0, 0, 0f, 0f, 0f, 0f, 1, 100)
                        }

                        override fun onHit(e: LivingEntity) {
                            projectile.remove()
                        }

                        override fun onHit() {
                            projectile.remove()
                        }
                    }
                }
            }
            startCooldown(e.player, 20L)
        } else if (e.click == AbilityUse.Click.RIGHT) {
            for (i in -15..15 step 15) {
                val arrow = e.player.world.spawnArrow(
                        e.player.eyeLocation,
                        e.player.eyeLocation.direction.rotateAroundY(i.toDouble()),
                        0.6f, 0f)
                arrow.setGravity(false)
                object : ProjectileShot(e.player, arrow) {
                    override fun whileFlying() {
                        projectile.world.spigot().playEffect(projectile.location,
                                Effect.MAGIC_CRIT, 0, 0, 0f, 0f, 0f, 0f, 1, 100)
                    }

                    override fun onHit(e: LivingEntity) {
                        projectile.remove()
                    }

                    override fun onHit() {
                        projectile.remove()
                    }
                }
            }
            startCooldown(e.player, 12L)
        }
    }
}

