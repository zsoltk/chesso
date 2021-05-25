package com.github.zsoltk.rf1.model.game.preset

import com.github.zsoltk.rf1.model.game.GameController

interface Preset {

    fun apply(gameController: GameController)
}
