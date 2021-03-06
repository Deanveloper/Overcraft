package com.deanveloper.overcraft.heroes

import com.deanveloper.overcraft.item.Interactive
import com.deanveloper.overcraft.util.OcPlayer

/**
 * @author Dean
 */
interface HeroBase {
    val items: List<Interactive>
    fun onSpawn(p: OcPlayer)
}