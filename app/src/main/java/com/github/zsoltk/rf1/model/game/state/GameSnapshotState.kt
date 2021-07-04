package com.github.zsoltk.rf1.model.game.state

import android.os.Parcelable
import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.move.AppliedMove
import com.github.zsoltk.rf1.model.move.BoardMove
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.move.MoveEffect
import com.github.zsoltk.rf1.model.move.targetPositions
import com.github.zsoltk.rf1.model.piece.King
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.WHITE
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSnapshotState(
    val boardState: BoardState = BoardState(),
    val resolution: Resolution = Resolution.IN_PROGRESS,
    val move: AppliedMove? = null,
    val lastMove: AppliedMove? = null,
    val castlingInfo: CastlingInfo = CastlingInfo.from(boardState.board),
    val capturedPieces: List<Piece> = emptyList()
) : Parcelable {

    val board: Board
        get() = boardState.board

    val toMove: Set
        get() = boardState.toMove

    val score: Int =
        capturedPieces.sumBy {
            it.value * if (it.set == WHITE) -1 else 1
        }

    val allLegalMoves by lazy {
        board.pieces.flatMap { (_, piece) ->
            piece
                .pseudoLegalMoves(this, false)
                .applyCheckConstraints()
        }
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

    fun legalMovesTo(position: Position): List<BoardMove> =
        allLegalMoves.filter { it.to == position }

    fun legalMovesFrom(position: Position): List<BoardMove> =
        allLegalMoves.filter { it.from == position }

    private fun List<BoardMove>.applyCheckConstraints(): List<BoardMove> =
        filter { move ->
            // Any move made should result in no check (clear current if any, and not cause a new one)
            val newGameState = derivePseudoGameState(move)
            !newGameState.hasCheckFor(move.piece.set)
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
        val insufficientMaterial = tempNewGameState.board.pieces.hasInsufficientMaterial()
        val threefoldRepetition = (boardStatesSoFar + tempNewGameState.boardState).hasThreefoldRepetition()

        val appliedMove = AppliedMove(
            boardMove = boardMove,
            effect = when {
                isCheckNoMate -> MoveEffect.CHECK
                isCheckMate -> MoveEffect.CHECKMATE
                isStaleMate -> MoveEffect.DRAW
                insufficientMaterial -> MoveEffect.DRAW
                threefoldRepetition -> MoveEffect.DRAW
                else -> null
            },
        )

        return GameStateTransition(
            move = appliedMove,
            fromSnapshotState = this.copy(
                move = appliedMove
            ),
            toSnapshotState = copy(
                boardState = tempNewGameState.boardState,
                resolution = when {
                    isCheckMate -> Resolution.CHECKMATE
                    isStaleMate -> Resolution.STALEMATE
                    threefoldRepetition -> Resolution.DRAW_BY_REPETITION
                    insufficientMaterial -> Resolution.INSUFFICIENT_MATERIAL
                    else -> Resolution.IN_PROGRESS
                },
                move = null,
                lastMove = appliedMove,
                castlingInfo = castlingInfo.apply(boardMove),
                capturedPieces = (boardMove.preMove as? Capture)?.let { capturedPieces + it.piece } ?: capturedPieces
            )
        )
    }

    private fun derivePseudoGameState(boardMove: BoardMove): GameSnapshotState = copy(
        boardState = boardState.deriveBoardState(boardMove),
        move = null,
        lastMove = AppliedMove(
            boardMove = boardMove,
            effect = null
        )
    )
}
