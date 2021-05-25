package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.BoardState
import com.github.zsoltk.rf1.model.move.Move
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Bishop(override val set: Set) : Piece {

    override val value: Int = 3

    override val symbol: String = when (set) {
        WHITE -> "♗"
        BLACK -> "♝"
    }

    override fun moves(boardState: BoardState): List<Move> =
        lineMoves(boardState, directions)

    companion object {
        val directions = listOf(
            -1 to -1,
            -1 to 1,
            1 to -1,
            1 to 1,
        )
    }
}
