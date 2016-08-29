package com.deanveloper.overcraft.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Dean
 */
class OcItem(mat: Material, count: Int, damage: Short) : ItemStack(mat, count, damage) {
    constructor(
            mat: Material,
            name: String = "",
            lore: List<String> = emptyList(),
            isUnbreakable: Boolean = true
    ) : this(mat, 1, 0) {
        this.name = name
        this.lore = lore
        this.isUnbreakable = isUnbreakable
    }

    constructor(mat: Material, damage: Short) : this(mat, 1, damage)

    var name: String
        get() = itemMeta?.displayName ?: ""
        set(value) {
            itemMeta = itemMeta?.apply {
                displayName = if(value.isEmpty()) null else value
            }
        }

    var lore: List<String>
        get() = itemMeta?.lore ?: emptyList()
        set(value) {
            itemMeta = itemMeta?.apply {
                lore = value
            }
        }

    var isUnbreakable: Boolean
        get() = itemMeta?.spigot()?.isUnbreakable ?: false
        set(value) {
            itemMeta = itemMeta?.apply {
                spigot()?.isUnbreakable = value
            }
        }
}