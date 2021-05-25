package com.github.zsoltk.rf1.model.game.preset

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.GameController

object CheckMateTest : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            applyMove(Position.e2, Position.e4)
            applyMove(Position.e7, Position.e5)
            applyMove(Position.d1, Position.h5)
            applyMove(Position.b8, Position.c6)
            applyMove(Position.f1, Position.c4)
            applyMove(Position.f8, Position.c5)
            onClick(Position.h5)
        }
    }
}
