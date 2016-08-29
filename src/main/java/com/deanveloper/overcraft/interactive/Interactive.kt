package com.deanveloper.overcraft.interactive

import com.deanveloper.kbukkit.plus
import com.deanveloper.overcraft.PLUGIN
import com.deanveloper.overcraft.util.Cooldowns
import com.deanveloper.overcraft.util.Interaction
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

/**
 * @author Dean
 */
abstract class Interactive : Listener {
    protected val cooldowns = Cooldowns()
    val item: ItemStack = ItemStack(Material.AIR)

    init {
        Bukkit.getPluginManager().registerEvents(this, PLUGIN)
        item.itemMeta = item.itemMeta.apply {
            this.displayName = name
            this.lore = this@Interactive.lore.map { ChatColor.GRAY + it }
        }
    }

    @EventHandler
    fun _onClick(e: PlayerInteractEvent) {
        if (e.action != Action.PHYSICAL) {
            if (this.isSimilar(e.item)) {
                e.isCancelled = true

                onClick(Interaction(
                    e.player,
                    this,
                    null,
                    e.action.toClick!!
            ))
            }
        }
    }

    @EventHandler
    fun _onClickPlayer(e: PlayerInteractEntityEvent) {
        if (e.rightClicked is LivingEntity) {
            if (this.isSimilar(e.player.inventory.itemInMainHand)) {
                e.isCancelled = true

                onClick(Interaction(
                        e.player,
                        this,
                        e.rightClicked as LivingEntity,
                        Interaction.Click.RIGHT
                ))
            }
        }
    }

    @EventHandler
    fun _onClickPlayer(e: EntityDamageByEntityEvent) {
        if (e.damager is Player && e.entity is LivingEntity) {
            val damager = e.damager as Player
            if (this.isSimilar(damager.inventory.itemInMainHand)) {
                e.isCancelled = true
                onClick(Interaction(
                        damager,
                        this,
                        e.entity as LivingEntity,
                        Interaction.Click.LEFT
                ))
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun _checkEquip(e: PlayerItemHeldEvent) {
        if (this.isSimilar(e.player.inventory.getItem(e.previousSlot))) {
            if(onUnEquip(e.player)) {
                // should move it back while not calling a second event?
                e.isCancelled = true
            }
        } else if (this.isSimilar(e.player.inventory.getItem(e.newSlot))) {
            if (onEquip(e.player)) {
                // should move it back while not cancelling the event
                e.player.inventory.heldItemSlot = e.previousSlot
            }
        }
    }

    /**
     * Type of the item
     */
    abstract val type: Material

    /**
     * Name of the item
     */
    abstract val name: String

    /**
     * Lore of the item
     */
    abstract val lore: List<String>

    /**
     * When the interactive is clicked
     */
    abstract fun onClick(e: Interaction)

    /**
     * When the interactive is equipped
     *
     * @return whether to move the cursor back to the main weapon
     */
    abstract fun onEquip(p: Player): Boolean

    /**
     * When the interactive is unequipped
     *
     * @return whether to keep the cursor on the item
     */
    abstract fun onUnEquip(p: Player): Boolean

    /**
     * Function to decide if an item is this type of item
     */
    fun isSimilar(item: ItemStack): Boolean {
        return item.itemMeta?.displayName == this.item.itemMeta?.displayName
                && item.itemMeta?.lore == this.item.itemMeta?.lore
                && item.typeId == this.item.typeId
    }
}