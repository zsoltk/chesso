package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.CurrentState
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Pawn(override val set: Set) : Piece {

    override val value: Int = 1

    override val symbol: String = when (set) {
        WHITE -> "♙"
        BLACK -> "♟︎"
    }

    override fun moves(currentState: CurrentState): List<Position> {
        val moves = mutableListOf<Position>()
        val board = currentState.board
        val square = board.find(this) ?: return moves


        baseMove(board, square)?.let { moves += it }
        initialMove(board, square)?.let { moves += it }

        return moves
    }

    private fun baseMove(
        board: Board,
        square: Square
    ): Position? {
        val delta = if (set == WHITE) 1 else -1
        val target = board[square.file, square.rank + delta]
        return if (target?.isEmpty == true) target.position else null
    }

    private fun initialMove(
        board: Board,
        square: Square
    ): Position? {
        if ((set == WHITE && square.rank == 2) || (set == BLACK && square.rank == 7)) {
            val delta1 = if (set == WHITE) 1 else -1
            val delta2 = if (set == WHITE) 2 else -2
            val target1 = board[square.file, square.rank + delta1]
            val target2 = board[square.file, square.rank + delta2]
            return if (target1?.isEmpty == true && target2?.isEmpty == true) target2.position else null
        }
        return null
    }
}
