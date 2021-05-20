package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Set.*

class Bishop(override val set: Set) : Piece {

    override val value: Int = 3

    override val symbol: String = when (set) {
        WHITE -> "♗"
        BLACK -> "♝"
    }

    override fun moves(gameState: GameState): List<Position> =
        lineMoves(gameState, directions)

    companion object {
        val directions = listOf(
            -1 to -1,
            -1 to 1,
            1 to -1,
            1 to 1,
        )
    }
}
