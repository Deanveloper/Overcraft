package com.deanveloper.overcraft.heroes

import com.deanveloper.overcraft.heroes.attack.Genji

/**
 * @author Dean
 */
enum class Heroes(lazyHero: () -> HeroBase) {
    GENJI({ Genji });

    val hero by lazy(lazyHero)
}