package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set

data class BoardState(
    val board: Board = Board(),
    val toMove: Set = Set.WHITE,
) {
    fun hasCheck(): Boolean =
        hasCheckFor(toMove)

    private fun hasCheckFor(set: Set): Boolean {
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
            val newBoardState = deriveBoardState(move.from, move.to)
            !newBoardState.hasCheckFor(toMove)
        }

    fun deriveBoardState(from: Position, to: Position): BoardState {
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
}