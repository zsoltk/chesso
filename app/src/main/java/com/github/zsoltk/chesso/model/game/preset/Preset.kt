package com.github.zsoltk.chesso.model.game.preset

import com.github.zsoltk.chesso.model.game.controller.GameController

interface Preset {

    fun apply(gameController: GameController)
}
