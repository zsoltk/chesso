package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.WHITE

data class GameState(
    val board: Board = Board(), // Extract to BoardState with calculations
    val resolution: Resolution = Resolution.IN_PROGRESS,
    val toMove: Set = WHITE, // Extract to BoardState
    val move: Move? = null,
    val lastMove: Move? = null,
    val capturedPieces: List<Piece> = emptyList()
) {
    val score: Int =
        capturedPieces.sumBy {
            it.value * if (it.set == WHITE) -1 else 1
        }

    fun hasCheckFor(set: Set): Boolean {
        val kingsPosition: Position? = board.pieces.keys.find { position ->
            val piece = board.pieces[position]
            piece is King && piece.set == set
        }

        return board.pieces.any { (_, piece) ->
            val otherPieceCaptures: List<Move> = piece.possibleCaptures(this) ?: emptyList()
            kingsPosition in otherPieceCaptures.targetPositions()
        }
    }

    fun legalMovesFrom(from: Position?): List<Move> =
        from.let { position ->
            possibleMovesWithoutCaptures(position).applyCheckConstraints()
        }

    fun legalCapturesFrom(from: Position?): List<Move> =
        from.let { position ->
            possibleCaptures(position).applyCheckConstraints()
        }

    private fun possibleMovesWithoutCaptures(from: Position?): List<Move> =
        map(from) { piece ->
            piece.movesWithoutCaptures(this)
        }

    private fun possibleCaptures(from: Position?): List<Move> =
        map(from) { piece ->
            piece.possibleCaptures(this)
        }

    private fun map(from: Position?, mapper: (Piece) -> List<Move>): List<Move> {
        var list = emptyList<Move>()

        from?.let { nonNullPosition ->
            val square = board[nonNullPosition]
            square.piece?.let { piece ->
                list = mapper(piece)
            }
        }

        return list
    }

    private fun List<Move>.applyCheckConstraints(): List<Move> =
        filter { move ->
            // Any move made should result in no check (clear current if any, and not cause a new one)
            val newBoardState = calculateNewBoardState(move.from, move.to)
            !newBoardState.hasCheckFor(this@GameState.toMove)
        }

    // Extract to BoardState
    private fun calculateNewBoardState(from: Position, to: Position): GameState {
        val pieceToMove = board[from].piece
        requireNotNull(pieceToMove)

        val updatedBoard = board.copy(
            pieces = board.pieces
                .minus(from)
                .plus(to to pieceToMove)
        )

        return copy(
            board = updatedBoard,
            toMove = toMove.opposite()
        )
    }

    fun calculateMove(from: Position, to: Position): CalculatedMove {
        val pieceToMove = board[from].piece
        val capturedPiece = board[to].piece
        requireNotNull(pieceToMove)

        val newBoardState = calculateNewBoardState(from, to)
        val nextToMove = toMove.opposite()

        val validMoves = newBoardState.board.pieces(nextToMove).filter { (position, _) ->
            newBoardState.legalMovesFrom(position).isNotEmpty() ||
                newBoardState.legalCapturesFrom(position).isNotEmpty()
        }
        val isCheck = newBoardState.hasCheckFor(nextToMove)
        val isCheckMate = isCheck && validMoves.isEmpty()
        val move = Move(
            from = from,
            to = to,
            piece = pieceToMove,
            isCapture = capturedPiece != null,
            isCheck = isCheck,
            isCheckMate = isCheckMate
        )

        return CalculatedMove(
            move = move,
            updatedCurrentState = this.copy(
                move = move
            ),
            newState = newBoardState.copy(
                resolution = if (isCheckMate) Resolution.CHECKMATE else Resolution.IN_PROGRESS,
                toMove = nextToMove,
                move = null,
                lastMove = move,
                capturedPieces = capturedPiece?.let { capturedPieces + it } ?: capturedPieces
            )
        )
    }
}
