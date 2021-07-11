package com.github.zsoltk.chesso.model.piece

import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.board.Board
import com.github.zsoltk.chesso.model.board.Square
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.move.BoardMove
import com.github.zsoltk.chesso.model.move.Capture
import com.github.zsoltk.chesso.model.move.Move
import com.github.zsoltk.chesso.model.move.Promotion
import com.github.zsoltk.chesso.model.piece.Set.BLACK
import com.github.zsoltk.chesso.model.piece.Set.WHITE
import kotlinx.parcelize.Parcelize

@Parcelize
class Pawn(override val set: Set) : Piece {

    override val value: Int = 1

    override val asset: Int =
        when (set) {
            WHITE -> R.drawable.pawn_light
            BLACK -> R.drawable.pawn_dark
        }

    override val symbol: String = when (set) {
        WHITE -> "♙"
        BLACK -> "♟︎"
    }

    override val textSymbol: String = ""

    override fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> {
        val board = gameSnapshotState.board
        val square = board.find(this) ?: return emptyList()
        val moves = mutableListOf<BoardMove>()

        advanceSingle(board, square)?.let { moves += it }
        advanceTwoSquares(board, square)?.let { moves += it }
        captureDiagonalLeft(board, square)?.let { moves += it }
        captureDiagonalRight(board, square)?.let { moves += it }
        enPassantCaptureLeft(gameSnapshotState, square)?.let { moves += it }
        enPassantCaptureRight(gameSnapshotState, square)?.let { moves += it }

        return moves.flatMap {
            it.checkForPromotion()
        }
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
        gameSnapshotState: GameSnapshotState,
        square: Square
    ): BoardMove? = enPassantDiagonal(gameSnapshotState, square, -1)

    private fun enPassantCaptureRight(
        gameSnapshotState: GameSnapshotState,
        square: Square
    ): BoardMove? = enPassantDiagonal(gameSnapshotState, square, 1)

    private fun enPassantDiagonal(
        gameSnapshotState: GameSnapshotState,
        square: Square,
        deltaFile: Int
    ): BoardMove? {
        if (square.position.rank != if (set == WHITE) 5 else 4) return null
        val lastMove = gameSnapshotState.lastMove ?: return null
        if (lastMove.piece !is Pawn) return null
        val fromInitialSquare = (lastMove.from.rank == if (set == WHITE) 7 else 2)
        val twoSquareMove = (lastMove.to.rank == square.position.rank)
        val isOnNextFile = lastMove.to.file == square.file + deltaFile

        return if (fromInitialSquare && twoSquareMove && isOnNextFile) {
            val deltaRank = if (set == WHITE) 1 else -1
            val enPassantTarget = gameSnapshotState.board[square.file + deltaFile, square.rank + deltaRank]
            val capturedPieceSquare = gameSnapshotState.board[square.file + deltaFile, square.rank]
            requireNotNull(enPassantTarget)
            requireNotNull(capturedPieceSquare)

            BoardMove(
                move = Move(this, square.position, enPassantTarget.position),
                preMove = Capture(capturedPieceSquare.piece!!, capturedPieceSquare.position)
            )
        } else null
    }
}

private fun BoardMove.checkForPromotion(): List<BoardMove> =
    if (move.to.rank == if (piece.set == WHITE) 8 else 1) {
        listOf(
            copy(consequence = Promotion(move.to, Queen(piece.set))),
            copy(consequence = Promotion(move.to, Rook(piece.set))),
            copy(consequence = Promotion(move.to, Bishop(piece.set))),
            copy(consequence = Promotion(move.to, Knight(piece.set))),
        )
    } else listOf(this)
