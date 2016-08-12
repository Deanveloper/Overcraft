package com.deanveloper.overcraft.interactive.offense

import com.deanveloper.kbukkit.plus
import com.deanveloper.kbukkit.runTaskLater
import com.deanveloper.kbukkit.runTaskTimer
import com.deanveloper.overcraft.Overcraft
import com.deanveloper.overcraft.interactive.Ability
import com.deanveloper.overcraft.interactive.Weapon
import com.deanveloper.overcraft.util.Interaction
import com.deanveloper.overcraft.util.ProjectileShot
import com.deanveloper.overcraft.util.rotateAroundY
import org.bukkit.ChatColor
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.LazyMetadataValue

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

    override fun onUse(e: Interaction) {
        if (e.click == Interaction.Click.LEFT) {
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
        } else if (e.click == Interaction.Click.RIGHT) {
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
                        e.damage(2.8, source)
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

object Reflect : Ability() {
    override val itemType = Material.THIN_GLASS
    override val name = "§dReflect"
    override val lore = listOf(
            "Reflect any projectiles away from you"
    )

    override fun onUse(i: Interaction) {
        val p = i.player
        val armorStand = p.world.spawn(p.location.add(p.location.direction), ArmorStand::class.java)
        armorStand.setMetadata("reflect", LazyMetadataValue(Overcraft.instance, { i.player }))

        val task = runTaskTimer(Overcraft.instance, 0L, 1L) {
            armorStand.teleport(p.location.add(p.location.direction))
        }

        runTaskLater(Overcraft.instance, 20L) {
            task.cancel()
        }

        startCooldown(p, 20)
    }
}