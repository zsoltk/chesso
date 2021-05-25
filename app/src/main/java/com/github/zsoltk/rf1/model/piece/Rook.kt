package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.BoardState
import com.github.zsoltk.rf1.model.game.Move
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Rook(override val set: Set) : Piece {

    override val value: Int = 5

    override val symbol: String = when (set) {
        WHITE -> "♖"
        BLACK -> "♜"
    }

    override fun moves(boardState: BoardState): List<Move> =
        lineMoves(boardState, directions)

    companion object {
        val directions = listOf(
            0 to -1,
            0 to 1,
            -1 to 0,
            1 to 0,
        )
    }
}
