package com.deanveloper.overcraft.interactive

import com.deanveloper.kbukkit.plus
import com.deanveloper.kbukkit.runTaskLater
import com.deanveloper.overcraft.Overcraft
import com.deanveloper.overcraft.util.AbilityUse
import com.deanveloper.overcraft.util.toClick
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import java.util.*

/**
 * @author Dean
 */
abstract class Interactive : ItemStack(), Listener {
    protected val COOLDOWNS = mutableMapOf<UUID, BukkitTask>()
    init {
        Bukkit.getPluginManager().registerEvents(this, Overcraft.instance)
        type = itemType
        itemMeta = itemMeta.apply {
            this.displayName = name
            this.lore = this@Interactive.lore.map { ChatColor.GRAY + it }
        }
    }

    @EventHandler
    fun _onClick(e: PlayerInteractEvent) {
        if (e.action != Action.PHYSICAL) {
            if (this.isSimilar(e.item))
                onClick(AbilityUse(e.player, e.item as Interactive, null, e.action.toClick!!))
        }
    }

    @EventHandler
    fun _onClickPlayer(e: PlayerInteractEntityEvent) {
        if (e.rightClicked is LivingEntity) {
            if (this.isSimilar(e.player.inventory.itemInMainHand))
                onClick(AbilityUse(
                        e.player, e.player.inventory.itemInMainHand as Interactive,
                        e.rightClicked as LivingEntity, AbilityUse.Click.RIGHT
                ))
        }
    }

    @EventHandler
    fun _onClickPlayer(e: EntityDamageByEntityEvent) {
        if (e.damager is Player && e.entity is LivingEntity) {
            val damager = e.damager as Player
            if (this.isSimilar(damager.inventory.itemInMainHand))
                onClick(AbilityUse(
                        damager, damager.inventory.itemInMainHand as Interactive,
                        e.entity as LivingEntity, AbilityUse.Click.LEFT
                ))
        }
    }

    @EventHandler
    fun _checkEquip(e: PlayerItemHeldEvent) {
        if (e.player.inventory.getItem(e.previousSlot).isSimilar(this)) {
            onUnEquip()
        } else if (e.player.inventory.getItem(e.newSlot).isSimilar(this)) {
            if (onEquip()) {
                // Might call the event again? Not sure. Will test, but hopefully it should
                e.player.inventory.heldItemSlot = 0
            }
        }
    }

    /**
     * Type of the item
     */
    abstract val itemType: Material

    /**
     * Name of the item
     */
    abstract val name: String

    /**
     * Lore of the item
     */
    abstract val lore: List<String>

    /**
     * Cause the item to be unusable for [player] for [time] ticks
     */
    fun startCooldown(player: UUID, time: Long): Boolean {
        if(onCooldown(player)) return true

        COOLDOWNS.put(player, runTaskLater(Overcraft.instance, time) {
            COOLDOWNS.remove(player)
        })

        return false
    }

    /**
     * Cause the item to be unusable for [player] for [time] ticks
     */
    fun startCooldown(player: Player, time: Long): Boolean = startCooldown(player.uniqueId, time)

    /**
     * Check if [player] can use this item
     */
    fun onCooldown(player: UUID): Boolean = COOLDOWNS.contains(player)

    /**
     * Check if [player] can use this item
     */
    fun onCooldown(player: Player): Boolean = onCooldown(player.uniqueId)

    /**
     * When the interactive is clicked
     */
    abstract fun onClick(e: AbilityUse)

    /**
     * When the interactive is equipped
     *
     * @return whether to move the cursor back to the main weapon
     */
    abstract fun onEquip(): Boolean

    /**
     * When the interactive is equipped
     */
    abstract fun onUnEquip()

    /**
     * Function to decide if an item is this type of item
     */
    override final fun isSimilar(item: ItemStack): Boolean {
        return item.itemMeta?.displayName == this.itemMeta?.displayName
                && item.itemMeta?.lore == this.itemMeta?.lore
                && item.typeId == this.typeId
    }
}