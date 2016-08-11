package com.deanveloper.overcraft.interactive

/**
 * @author Dean
 */
abstract class Weapon : Interactive() {
    override final fun onEquip() = false
    override final fun onUnEquip() {}
}