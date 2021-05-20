package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.notation.Position

interface Piece {

    val set: Set

    val symbol: String

    val value: Int

    fun moves(gameState: GameState): List<Position> = emptyList()
}
