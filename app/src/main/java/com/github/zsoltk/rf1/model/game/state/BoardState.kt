package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.piece.Set

data class BoardState(
    val board: Board = Board(),
    val toMove: Set = Set.WHITE,
) {

    fun deriveBoardState(boardMove: BoardMove): BoardState {
        val updatedBoard = board
            .apply(boardMove.consequence)
            .apply(boardMove.move)

        return copy(
            board = updatedBoard,
            toMove = toMove.opposite()
        )
    }
}
