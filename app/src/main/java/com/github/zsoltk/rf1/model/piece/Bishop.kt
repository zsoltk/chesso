package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Set.*

class Bishop(override val set: Set) : Piece {

    override val value: Int = 3

    override val symbol: String = when (set) {
        WHITE -> "♗"
        BLACK -> "♝"
    }

    override fun moves(gameState: GameState): List<Position> {
        val moves = mutableListOf<Position>()
        val board = gameState.board
        val square = board.find(this) ?: return emptyList()

        val directions = listOf(
            -1 to -1,
            -1 to 1,
            1 to -1,
            1 to 1,
        )

        directions.map {
            moves += lineMoves(board, square, it.first, it.second)
        }

        return moves
    }

    fun lineMoves(
        board: Board,
        square: Square,
        deltaFile: Int,
        deltaRank: Int
    ): List<Position> {
        val moves = mutableListOf<Position>()

        var i = 0
        while (true) {
            i++
            val target = board[square.file + deltaFile * i, square.rank + deltaRank * i] ?: break
            if (target.isEmpty) {
                moves += target.position
                continue
            }
            if (target.hasPiece(set.opposite())) {
                moves += target.position
                break
            }
            if (target.hasPiece(set)) {
                break
            }
        }

        return moves
    }
}
