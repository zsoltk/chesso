package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.CalculatedMove
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.move.MoveEffect
import com.github.zsoltk.rf1.model.move.MoveIntention
import com.github.zsoltk.rf1.model.move.targetPositions
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.WHITE

data class GameState(
    val boardState: BoardState = BoardState(),
    val resolution: Resolution = Resolution.IN_PROGRESS,
    val move: CalculatedMove? = null,
    val lastMove: CalculatedMove? = null,
    val capturedPieces: List<Piece> = emptyList()
) {
    val board: Board
        get() = boardState.board

    private val toMove: Set
        get() = boardState.toMove

    val score: Int =
        capturedPieces.sumBy {
            it.value * if (it.set == WHITE) -1 else 1
        }

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
            val newGameState = derivePseudoGameState(move)
            !newGameState.hasCheckFor(boardState.toMove)
        }


    fun calculateAppliedMove(moveIntention: MoveIntention, boardStatesSoFar: List<BoardState>): AppliedMove {
        val pieceToMove = board[moveIntention.from].piece
        requireNotNull(pieceToMove)

        val pieceMoves = legalMovesFrom(moveIntention.from)
        val boardMove = pieceMoves.find { it.to == moveIntention.to }
        requireNotNull(boardMove)

        val tempNewGameState = derivePseudoGameState(boardMove)
        val nextToMove = boardState.toMove.opposite()

        val validMoves = tempNewGameState.board.pieces(nextToMove).filter { (position, _) ->
            tempNewGameState.legalMovesFrom(position).isNotEmpty()
        }
        val isCheck = tempNewGameState.hasCheck()
        val isCheckNoMate = validMoves.isNotEmpty() && isCheck
        val isCheckMate = validMoves.isEmpty() && isCheck
        val isStaleMate = validMoves.isEmpty() && !isCheck
        val threefoldRepetition = (boardStatesSoFar + tempNewGameState.boardState).hasThreefoldRepetition()

        val calculatedMove = CalculatedMove(
            boardMove = boardMove,
            effect = when {
                isCheckNoMate -> MoveEffect.CHECK
                isCheckMate -> MoveEffect.CHECKMATE
                isStaleMate -> MoveEffect.DRAW
                threefoldRepetition -> MoveEffect.DRAW
                else -> null
            },
        )

        return AppliedMove(
            move = calculatedMove,
            updatedCurrentState = this.copy(
                move = calculatedMove
            ),
            newState = copy(
                boardState = tempNewGameState.boardState,
                resolution = when {
                    isCheckMate -> Resolution.CHECKMATE
                    isStaleMate -> Resolution.STALEMATE
                    threefoldRepetition -> Resolution.DRAW_BY_REPETITION
                    else -> Resolution.IN_PROGRESS
                },
                move = null,
                lastMove = calculatedMove,
                capturedPieces = (boardMove.consequence as? Capture)?.let { capturedPieces + it.piece } ?: capturedPieces
            )
        )
    }

    fun derivePseudoGameState(boardMove: BoardMove): GameState = copy(
        boardState = boardState.deriveBoardState(boardMove)
    )
}
