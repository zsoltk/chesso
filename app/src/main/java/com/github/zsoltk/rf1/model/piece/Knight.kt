package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.state.GameSnaphotState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Knight(override val set: Set) : Piece {

    override val value: Int = 3

    override val symbol: String = when (set) {
        WHITE -> "♘"
        BLACK -> "♞"
    }

    override val textSymbol: String = "N"

    override fun pseudoLegalMoves(gameSnaphotState: GameSnaphotState, checkCheck: Boolean): List<BoardMove> =
        targets
            .map { singleCaptureMove(gameSnaphotState, it.first, it.second) }
            .filterNotNull()

    companion object {
        val targets = listOf(
            -2 to 1,
            -2 to -1,
            2 to 1,
            2 to -1,
            1 to 2,
            1 to -2,
            -1 to 2,
            -1 to -2
        )
    }
}
