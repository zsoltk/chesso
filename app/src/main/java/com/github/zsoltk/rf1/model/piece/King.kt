package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.game.Move
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class King(override val set: Set) : Piece {

    override val value: Int = Int.MAX_VALUE

    override val symbol: String = when (set) {
        WHITE -> "♔"
        BLACK -> "♚"
    }

    override fun moves(gameState: GameState): List<Move> {
        val board = gameState.board
        val square = board.find(this) ?: return emptyList()

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

        return targets
            .map { move(board, square, it.first, it.second) }
            .filterNotNull()
    }

    private fun move(
        board: Board,
        square: Square,
        deltaFile: Int,
        deltaRank: Int
    ): Move? {
        val target = board[square.file + deltaFile, square.rank + deltaRank]
        return if (target?.isEmpty == true || target?.hasPiece(set.opposite()) == true) Move(
            from = square.position,
            to = target.position,
            piece = this
        ) else null
    }
}
