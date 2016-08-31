package com.deanveloper.overcraft.item

import com.deanveloper.overcraft.PLUGIN
import com.deanveloper.overcraft.util.Cooldowns
import com.deanveloper.overcraft.util.Interaction
import com.deanveloper.overcraft.util.OcItem
import com.deanveloper.overcraft.util.toClick
import org.bukkit.Bukkit
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.lang.ref.WeakReference

/**
 * @author Dean
 */
abstract class Interactive : Listener {
    protected val cooldowns = Cooldowns()

    abstract val items: ItemPair

    abstract val slot: Int

    abstract val cooldown: Long

    init {
        Bukkit.getPluginManager().registerEvents(this, PLUGIN)
    }

    @EventHandler
    fun _onClick(e: PlayerInteractEvent) {
        if(e.hand === EquipmentSlot.HAND) {
            if (e.action != Action.PHYSICAL) {
                if (slot === e.player.inventory.heldItemSlot) {
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
    }

    @EventHandler
    fun _onDrop(e: PlayerDropItemEvent) {
        if(e.player.inventory.heldItemSlot == slot) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun _onInventory(e: InventoryClickEvent) {
        if(e.slot == slot) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun _onClickPlayer(e: PlayerInteractEntityEvent) {
        if (e.rightClicked is LivingEntity) {
            if (slot === e.player.inventory.heldItemSlot) {
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
        if(e.cause === EntityDamageEvent.DamageCause.CUSTOM) return
        val damager = e.damager
        val entity = e.entity
        if (damager is Player && entity is LivingEntity) {
            if (slot === damager.inventory.heldItemSlot) {
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
        if (slot === e.previousSlot) {
            if(onUnEquip(e.player)) {
                // should move it back while not calling a second event?
                e.isCancelled = true
            }
        } else if (slot === e.newSlot) {
            if (onEquip(e.player)) {
                // should move it back while not cancelling the event
                e.player.inventory.heldItemSlot = e.previousSlot
            }
        }
    }

    fun startCooldown(p: Player) {
        p.inventory.setItem(slot, items.cooldown)
        val pref = WeakReference(p)
        cooldowns.addCooldown(p, cooldown) {
            pref.get()?.inventory?.setItem(slot, items.main)
        }
    }

    /**
     * When the item is clicked
     */
    abstract fun onClick(e: Interaction)

    /**
     * When the item is equipped
     *
     * @return whether to move the cursor back to the main weapon
     */
    abstract fun onEquip(p: Player): Boolean

    /**
     * When the item is unequipped
     *
     * @return whether to keep the cursor on the item
     */
    abstract fun onUnEquip(p: Player): Boolean
}

data class ItemPair(val main: ItemStack, val cooldown: ItemStack) {
    companion object {
        val DEFAULT_ITEM: OcItem
            get() = OcItem(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.woolData.toShort())
    }
    constructor(main: ItemStack, toDefaultCooldown: Boolean) : this(
            main,
            if(toDefaultCooldown) {
                main.clone().apply {
                    type = Material.STAINED_GLASS_PANE
                    data.data = DyeColor.GRAY.woolData
                }
            } else {
                main
            })
}