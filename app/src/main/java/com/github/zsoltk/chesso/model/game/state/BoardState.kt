package com.github.zsoltk.chesso.model.game.state

import android.os.Parcelable
import com.github.zsoltk.chesso.model.board.Board
import com.github.zsoltk.chesso.model.move.BoardMove
import com.github.zsoltk.chesso.model.piece.Set
import kotlinx.parcelize.Parcelize

@Parcelize
data class BoardState(
    val board: Board = Board(),
    val toMove: Set = Set.WHITE,
) : Parcelable {

    fun deriveBoardState(boardMove: BoardMove): BoardState {
        val updatedBoard = board
            .apply(boardMove.preMove)
            .apply(boardMove.move)
            .apply(boardMove.consequence)

        return copy(
            board = updatedBoard,
            toMove = toMove.opposite()
        )
    }
}
