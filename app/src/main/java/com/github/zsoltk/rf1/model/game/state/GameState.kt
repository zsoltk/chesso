package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.AppliedMove
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.move.MoveEffect
import com.github.zsoltk.rf1.model.move.targetPositions
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.WHITE

data class GameState(
    val boardState: BoardState = BoardState(),
    val resolution: Resolution = Resolution.IN_PROGRESS,
    val move: AppliedMove? = null,
    val lastMove: AppliedMove? = null,
    val castlingInfo: CastlingInfo = CastlingInfo(),
    val capturedPieces: List<Piece> = emptyList()
) {

    val board: Board
        get() = boardState.board

    val toMove: Set
        get() = boardState.toMove

    val score: Int =
        capturedPieces.sumBy {
            it.value * if (it.set == WHITE) -1 else 1
        }

    fun hasCheck(): Boolean =
        hasCheckFor(toMove)

    fun hasCheckFor(set: Set): Boolean {
        val kingsPosition: Position = board.find<King>(set).firstOrNull()?.position ?: return false

        return hasCheckFor(kingsPosition)
    }

    fun hasCheckFor(position: Position): Boolean =
        board.pieces.any { (_, piece) ->
            val otherPieceCaptures: List<BoardMove> = piece.pseudoLegalMoves(this, true)
                .filter { it.preMove is Capture }

            position in otherPieceCaptures.targetPositions()
        }

    fun legalMovesFrom(from: Position): List<BoardMove> {
        val square = board[from]
        val piece = square.piece ?: return emptyList()
        return piece.pseudoLegalMoves(this, false).applyCheckConstraints()
    }

    private fun List<BoardMove>.applyCheckConstraints(): List<BoardMove> =
        filter { move ->
            // Any move made should result in no check (clear current if any, and not cause a new one)
            val newGameState = derivePseudoGameState(move)
            !newGameState.hasCheckFor(boardState.toMove)
        }


    fun calculateAppliedMove(boardMove: BoardMove, boardStatesSoFar: List<BoardState>): GameStateTransition {
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

        val appliedMove = AppliedMove(
            boardMove = boardMove,
            effect = when {
                isCheckNoMate -> MoveEffect.CHECK
                isCheckMate -> MoveEffect.CHECKMATE
                isStaleMate -> MoveEffect.DRAW
                threefoldRepetition -> MoveEffect.DRAW
                else -> null
            },
        )

        return GameStateTransition(
            move = appliedMove,
            updatedCurrentState = this.copy(
                move = appliedMove
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
                lastMove = appliedMove,
                castlingInfo = castlingInfo.apply(boardMove),
                capturedPieces = (boardMove.preMove as? Capture)?.let { capturedPieces + it.piece } ?: capturedPieces
            )
        )
    }

    fun derivePseudoGameState(boardMove: BoardMove): GameState = copy(
        boardState = boardState.deriveBoardState(boardMove),
        move = null,
        lastMove = AppliedMove(
            boardMove = boardMove,
            effect = null
        )
    )
}
