package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.WHITE

data class GameState(
    val board: Board = Board(),
    val toMove: Set = WHITE,
    val move: Move? = null,
    val lastMove: Move? = null,
    val capturedPieces: List<Piece> = emptyList()
) {
    val score: Int =
        capturedPieces.sumBy {
            it.value * if (it.set == WHITE) 1 else -1
        }
}
