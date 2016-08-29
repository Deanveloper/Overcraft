package com.deanveloper.overcraft.interactive.offense

import com.deanveloper.kbukkit.plus
import com.deanveloper.kbukkit.runTaskLater
import com.deanveloper.kbukkit.runTaskTimer
import com.deanveloper.overcraft.PLUGIN
import com.deanveloper.overcraft.interactive.Ability
import com.deanveloper.overcraft.interactive.ItemPair
import com.deanveloper.overcraft.interactive.Ultimate
import com.deanveloper.overcraft.interactive.Weapon
import com.deanveloper.overcraft.oc
import com.deanveloper.overcraft.util.*
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.LazyMetadataValue

/**
 * @author Dean
 */
object Shuriken : Weapon() {
    override val cooldown: Long
        get() = throw UnsupportedOperationException("Custom cooldown impl")

    override val items = ItemPair(
            OcItem(Material.NETHER_STAR,
                    ChatColor.GREEN + "Shuriken",
                    listOf(
                            "Left Click",
                            " - Shoot three shurikens in a row",
                            "",
                            "Right Click",
                            " - Shoot three shurikens in a fan pattern"
                    )
            ),
            toDefaultCooldown = false)

    override fun onUse(e: Interaction) {
        if (e.click == Interaction.Click.LEFT) {
            for (i in 0..11 step 4) {
                runTaskLater(PLUGIN, i.toLong()) {
                    val arrow = e.player.world.spawnArrow(
                            e.player.eyeLocation,
                            e.player.eyeLocation.direction,
                            2.5f,
                            0f)
                    arrow.setGravity(false)
                    arrow.world.playSound(arrow.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, .8f)
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
            cooldowns.addCooldown(e.player, 20L)
        } else if (e.click == Interaction.Click.RIGHT) {
            for (i in -15..15 step 15) {
                val arrow = e.player.world.spawnArrow(
                        e.player.eyeLocation,
                        e.player.eyeLocation.direction.rotateAroundY(i.toDouble()),
                        2.5f, 0f)
                arrow.setGravity(false)
                arrow.world.playSound(arrow.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, .8f)
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
            cooldowns.addCooldown(e.player, 12L)
        }
    }
}

object Reflect : Ability() {
    override val slot = 1
    override val items = ItemPair(
            ItemPair.DEFAULT_ITEM.apply {
                name = ChatColor.LIGHT_PURPLE + "Reflect"
                lore = listOf(
                        "Reflects projectiles in front of you",
                        "in the direction you are looking"
                )
            }, true)

    override val cooldown = 20 * 8L

    override fun onUse(i: Interaction) {
        val p = i.player
        val armorStand = p.world.spawn(p.location.add(p.location.direction), ArmorStand::class.java)
        armorStand.setMetadata("reflect", LazyMetadataValue(PLUGIN, { i.player }))
        i.player.oc.removeOnDeath.add(armorStand)

        val task = runTaskTimer(PLUGIN, 0, 1) {
            armorStand.teleport(p.location.add(p.location.direction.setY(0)))
        }

        val otherTask = runTaskTimer(PLUGIN, 0, 4) {
            armorStand.world.playSound(armorStand.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, .5f, 1.5f)
        }

        runTaskLater(PLUGIN, 20 * 2) {
            armorStand.remove()
            task.cancel()
            otherTask.cancel()

            startCooldown(p)
        }
    }
}

object SwiftStrike : Ability() {
    override val items = ItemPair(
            OcItem(Material.FEATHER,
                    ChatColor.LIGHT_PURPLE + "Swift Strike",
                    listOf(
                            "Move forward with extreme speed,",
                            "damaging enemies as you pass them"
                    )
            ), toDefaultCooldown = true)

    override val slot = 2

    override val cooldown = 8 * 20L

    override fun onUse(i: Interaction) {
        val from = i.player.location.clone()
        val to = from.clone().add(from.direction.multiply(14))

        object : HitscanShot(i.player) {
            override fun whileFlying(loc: Location): Boolean {
                if (loc.distanceSquared(to) < 1) {
                    i.player.teleport(to)
                    return false
                }
                return true
            }

            override fun onHit(e: LivingEntity): Boolean {
                e.damage(5.0, source)
                return true
            }

            override fun onHit(loc: Location): Boolean {
                i.player.teleport(loc.clone().subtract(loc.direction.multiply(.2)))
                return false
            }
        }

        startCooldown(i.player)
    }
}

object Dragonblade : Ultimate(true) {
    override val cooldown: Long
        get() = 20L
    override val slot = 3
    override val items = ItemPair(
            OcItem(
                    Material.DIAMOND_SWORD,
                    ChatColor.GREEN + ChatColor.BOLD + "Swift Strike",
                    listOf(
                            "Wield your sword, which deals",
                            "an extremely large amount of damage"
                    )
            ),
            OcItem(
                    Material.IRON_SWORD,
                    ChatColor.GREEN + ChatColor.BOLD + "Swift Strike",
                    listOf(
                            "Wield your sword, which deals",
                            "an extremely large amount of damage"
                    )
            )
    )

    override val honorBound = true

    override fun onUse(i: Interaction) {
        runTaskTimer(PLUGIN, 0, 5) {
            i.player.world.spigot().playEffect(
                    i.player.location.add(0.0, 1.0, 0.0).add(i.player.location.direction.multiply(-1)),
                    Effect.MAGIC_CRIT,
                    0, 0, // id and data (for block break and item crack)
                    .5f, 2f, .5f, // offsets
                    0.0f, 20, 30 // speed, number of particles, viewing radius
            )
            percent -= 1.0 / 24.0
        }
    }

    override fun onAttack(i: Interaction) {
        i.target?.damage(8.0)
        startCooldown(i.player)
    }
}