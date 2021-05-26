package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Queen(override val set: Set) : Piece {

    override val value: Int = 9

    override val symbol: String = when (set) {
        WHITE -> "♕"
        BLACK -> "♛"
    }

    override fun pseudoLegalMoves(gameState: GameState): List<BoardMove> =
        lineMoves(gameState, Rook.directions + Bishop.directions)
}
