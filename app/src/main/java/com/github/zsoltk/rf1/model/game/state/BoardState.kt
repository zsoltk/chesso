package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.move.targetPositions
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.piece.King
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
            val otherPieceCaptures: List<BoardMove> = piece.pseudoLegalMoves(this)
                    .filter { it.consequence is Capture }

            kingsPosition in otherPieceCaptures.targetPositions()
        }
    }

    fun legalMovesFrom(from: Position): List<BoardMove> {
        val square = board[from]
        val piece = square.piece ?: return emptyList()
        return piece.pseudoLegalMoves(this).applyCheckConstraints()
    }

    private fun List<BoardMove>.applyCheckConstraints(): List<BoardMove> =
        filter { move ->
            // Any move made should result in no check (clear current if any, and not cause a new one)
            val newBoardState = deriveBoardState(move)
            !newBoardState.hasCheckFor(toMove)
        }

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
