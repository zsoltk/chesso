package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.state.GameSnaphotState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Queen(override val set: Set) : Piece {

    override val value: Int = 9

    override val symbol: String = when (set) {
        WHITE -> "♕"
        BLACK -> "♛"
    }

    override val textSymbol: String = "Q"

    override fun pseudoLegalMoves(gameSnaphotState: GameSnaphotState, checkCheck: Boolean): List<BoardMove> =
        lineMoves(gameSnaphotState, Rook.directions + Bishop.directions)
}
