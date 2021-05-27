package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.move.Move
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE

class Pawn(override val set: Set) : Piece {

    override val value: Int = 1

    override val symbol: String = when (set) {
        WHITE -> "♙"
        BLACK -> "♟︎"
    }

    override fun pseudoLegalMoves(gameState: GameState): List<BoardMove> {
        val board = gameState.board
        val square = board.find(this) ?: return emptyList()
        val moves = mutableListOf<BoardMove>()

        advanceSingle(board, square)?.let { moves += it }
        advanceTwoSquares(board, square)?.let { moves += it }
        captureDiagonalLeft(board, square)?.let { moves += it }
        captureDiagonalRight(board, square)?.let { moves += it }
        enPassantCaptureLeft(gameState, square)?.let { moves += it }
        enPassantCaptureRight(gameState, square)?.let { moves += it }

        return moves
    }

    private fun advanceSingle(
        board: Board,
        square: Square
    ): BoardMove? {
        val deltaRank = if (set == WHITE) 1 else -1
        val target = board[square.file, square.rank + deltaRank]
        return if (target?.isEmpty == true) BoardMove(
            move = Move(this, square.position, target.position)
        ) else null
    }

    private fun advanceTwoSquares(
        board: Board,
        square: Square
    ): BoardMove? {
        if ((set == WHITE && square.rank == 2) || (set == BLACK && square.rank == 7)) {
            val deltaRank1 = if (set == WHITE) 1 else -1
            val deltaRank2 = if (set == WHITE) 2 else -2
            val target1 = board[square.file, square.rank + deltaRank1]
            val target2 = board[square.file, square.rank + deltaRank2]
            return if (target1?.isEmpty == true && target2?.isEmpty == true) BoardMove(
                move = Move(this, square.position, target2.position)
            ) else null
        }
        return null
    }

    private fun captureDiagonalLeft(
        board: Board,
        square: Square
    ): BoardMove? = captureDiagonal(board, square, -1)

    private fun captureDiagonalRight(
        board: Board,
        square: Square
    ): BoardMove? = captureDiagonal(board, square, 1)

    private fun captureDiagonal(
        board: Board,
        square: Square,
        deltaFile: Int
    ): BoardMove? {
        val deltaRank = if (set == WHITE) 1 else -1
        val target = board[square.file + deltaFile, square.rank + deltaRank]
        return if (target?.hasPiece(set.opposite()) == true) BoardMove(
            move = Move(this, square.position, target.position),
            preMove = Capture(target.piece!!, target.position)
        ) else null
    }

    private fun enPassantCaptureLeft(
        gameState: GameState,
        square: Square
    ): BoardMove? = enPassantDiagonal(gameState, square, -1)

    private fun enPassantCaptureRight(
        gameState: GameState,
        square: Square
    ): BoardMove? = enPassantDiagonal(gameState, square, 1)

    private fun enPassantDiagonal(
        gameState: GameState,
        square: Square,
        deltaFile: Int
    ): BoardMove? {
        if (square.position.rank != if (set == WHITE) 5 else 4) return null
        val lastMove = gameState.lastMove ?: return null
        if (lastMove.piece !is Pawn) return null
        val fromInitialSquare = (lastMove.from.rank == if (set == WHITE) 7 else 2)
        val twoSquareMove = (lastMove.to.rank == square.position.rank)
        val isOnNextFile = lastMove.to.file == square.file + deltaFile

        return if (fromInitialSquare && twoSquareMove && isOnNextFile) {
            val deltaRank = if (set == WHITE) 1 else -1
            val enPassantTarget = gameState.board[square.file + deltaFile, square.rank + deltaRank]
            val capturedPieceSquare = gameState.board[square.file + deltaFile, square.rank]
            requireNotNull(enPassantTarget)
            requireNotNull(capturedPieceSquare)

            BoardMove(
                move = Move(this, square.position, enPassantTarget.position),
                preMove = Capture(capturedPieceSquare.piece!!, capturedPieceSquare.position)
            )
        } else null
    }
}
