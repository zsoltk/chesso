package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.notation.Position
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

    fun calculateAppliedMove(moveIntention: MoveIntention): AppliedMove {
        val pieceToMove = board[moveIntention.from].piece
        val capturedPiece = board[moveIntention.to].piece
        requireNotNull(pieceToMove)

        val move = Move(moveIntention, pieceToMove)
        val newBoardState = boardState.deriveBoardState(move)
        val nextToMove = boardState.toMove.opposite()

        val validMoves = newBoardState.board.pieces(nextToMove).filter { (position, _) ->
            newBoardState.legalMovesFrom(position).isNotEmpty() ||
                newBoardState.legalCapturesFrom(position).isNotEmpty()
        }
        val isCheck = newBoardState.hasCheck()
        val isCheckMate = isCheck && validMoves.isEmpty()
        val calculatedMove = CalculatedMove(
            move = move,
            isCapture = capturedPiece != null,
            isCheck = isCheck,
            isCheckMate = isCheckMate
        )

        return AppliedMove(
            move = calculatedMove,
            updatedCurrentState = this.copy(
                move = calculatedMove
            ),
            newState = copy(
                boardState = newBoardState,
                resolution = if (isCheckMate) Resolution.CHECKMATE else Resolution.IN_PROGRESS,
                move = null,
                lastMove = calculatedMove,
                capturedPieces = capturedPiece?.let { capturedPieces + it } ?: capturedPieces
            )
        )
    }
}
