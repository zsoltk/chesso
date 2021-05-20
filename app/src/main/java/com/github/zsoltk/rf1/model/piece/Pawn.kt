package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Pawn(override val set: Set) : Piece {

    override val value: Int = 1

    override val symbol: String = when (set) {
        WHITE -> "♙"
        BLACK -> "♟︎"
    }

    override fun moves(gameState: GameState): List<Position> {
        val moves = mutableListOf<Position>()
        val board = gameState.board
        val square = board.find(this) ?: return moves


        advanceSingle(board, square)?.let { moves += it }
        advanceTwoSquares(board, square)?.let { moves += it }
        captureDiagonalLeft(board, square)?.let { moves += it }
        captureDiagonalRight(board, square)?.let { moves += it }

        return moves
    }

    private fun advanceSingle(
        board: Board,
        square: Square
    ): Position? {
        val deltaRank = if (set == WHITE) 1 else -1
        val target = board[square.file, square.rank + deltaRank]
        return if (target?.isEmpty == true) target.position else null
    }

    private fun advanceTwoSquares(
        board: Board,
        square: Square
    ): Position? {
        if ((set == WHITE && square.rank == 2) || (set == BLACK && square.rank == 7)) {
            val deltaRank1 = if (set == WHITE) 1 else -1
            val deltaRank2 = if (set == WHITE) 2 else -2
            val target1 = board[square.file, square.rank + deltaRank1]
            val target2 = board[square.file, square.rank + deltaRank2]
            return if (target1?.isEmpty == true && target2?.isEmpty == true) target2.position else null
        }
        return null
    }

    private fun captureDiagonalLeft(
        board: Board,
        square: Square
    ): Position? = captureDiagonal(board, square, -1)

    private fun captureDiagonalRight(
        board: Board,
        square: Square
    ): Position? = captureDiagonal(board, square, 1)

    private fun captureDiagonal(
        board: Board,
        square: Square,
        deltaFile: Int
    ): Position? {
        val deltaRank = if (set == WHITE) 1 else -1
        val target = board[square.file + deltaFile, square.rank + deltaRank]
        return if (target?.hasPiece(set.opposite()) == true) target.position else null
    }
}
