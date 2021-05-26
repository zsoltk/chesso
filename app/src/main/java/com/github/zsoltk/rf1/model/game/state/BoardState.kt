package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.move.targetPositions
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.move.BoardMove
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
            val otherPieceCaptures: List<BoardMove> = piece.possibleCaptures(this)
            kingsPosition in otherPieceCaptures.targetPositions()
        }
    }

    fun legalMovesFrom(from: Position?): List<BoardMove> =
        from.let { position ->
            possibleMovesWithoutCaptures(position).applyCheckConstraints()
        }

    fun legalCapturesFrom(from: Position?): List<BoardMove> =
        from.let { position ->
            possibleCaptures(position).applyCheckConstraints()
        }

    private fun possibleMovesWithoutCaptures(from: Position?): List<BoardMove> =
        map(from) { piece ->
            piece.movesWithoutCaptures(this)
        }

    private fun possibleCaptures(from: Position?): List<BoardMove> =
        map(from) { piece ->
            piece.possibleCaptures(this)
        }

    private fun map(from: Position?, mapper: (Piece) -> List<BoardMove>): List<BoardMove> {
        var list = emptyList<BoardMove>()

        from?.let { nonNullPosition ->
            val square = board[nonNullPosition]
            square.piece?.let { piece ->
                list = mapper(piece)
            }
        }

        return list
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
