package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.state.BoardState
import com.github.zsoltk.rf1.model.move.Move
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class King(override val set: Set) : Piece {

    override val value: Int = Int.MAX_VALUE

    override val symbol: String = when (set) {
        WHITE -> "♔"
        BLACK -> "♚"
    }

    override fun moves(boardState: BoardState): List<Move> =
        targets
            .map { singleCaptureMove(boardState, it.first, it.second) }
            .filterNotNull()

    companion object {
        val targets = listOf(
            -1 to -1,
            -1 to 0,
            -1 to 1,
            0 to 1,
            0 to -1,
            1 to -1,
            1 to 0,
            1 to 1,
        )
    }
}
