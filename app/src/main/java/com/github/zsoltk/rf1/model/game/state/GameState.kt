package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.move.CalculatedMove
import com.github.zsoltk.rf1.model.move.Capture
import com.github.zsoltk.rf1.model.move.MoveEffect
import com.github.zsoltk.rf1.model.move.MoveIntention
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set.WHITE

data class GameState(
    val boardState: BoardState = BoardState(),
    val resolution: Resolution = Resolution.IN_PROGRESS,
    val move: CalculatedMove? = null,
    val lastMove: CalculatedMove? = null,
    val capturedPieces: List<Piece> = emptyList()
) {
    private val board: Board
        get() = boardState.board

    val score: Int =
        capturedPieces.sumBy {
            it.value * if (it.set == WHITE) -1 else 1
        }

    fun calculateAppliedMove(moveIntention: MoveIntention, boardStatesSoFar: List<BoardState>): AppliedMove {
        val pieceToMove = board[moveIntention.from].piece
        requireNotNull(pieceToMove)

        val pieceMoves = boardState.legalMovesFrom(moveIntention.from) + boardState.legalCapturesFrom(moveIntention.from)
        val boardMove = pieceMoves.find { it.to == moveIntention.to }
        requireNotNull(boardMove)

        val newBoardState = boardState.deriveBoardState(boardMove)
        val nextToMove = boardState.toMove.opposite()

        val validMoves = newBoardState.board.pieces(nextToMove).filter { (position, _) ->
            newBoardState.legalMovesFrom(position).isNotEmpty() ||
                newBoardState.legalCapturesFrom(position).isNotEmpty()
        }
        val isCheck = newBoardState.hasCheck()
        val isCheckNoMate = validMoves.isNotEmpty() && isCheck
        val isCheckMate = validMoves.isEmpty() && isCheck
        val isStaleMate = validMoves.isEmpty() && !isCheck
        val threefoldRepetition = (boardStatesSoFar + newBoardState).hasThreefoldRepetition()

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
                boardState = newBoardState,
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
}
