package com.github.zsoltk.rf1.model.move

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set.WHITE

data class AppliedMove(
    val boardMove: BoardMove,
    val effect: MoveEffect? = null
) {
    val move: PrimaryPieceMove = boardMove.move

    val from: Position = move.from

    val to: Position = move.to

    val piece: Piece = move.piece

    override fun toString(): String {
        val isCapture = boardMove.preMove is Capture
        val symbol = when {
            piece !is Pawn -> piece.symbol
            isCapture -> from.fileAsLetter
            else -> ""
        }
        val capture = if (isCapture) "x" else ""
        val postFix = when (effect) {
            MoveEffect.CHECK -> "+"
            MoveEffect.CHECKMATE -> "#  ${if (boardMove.move.piece.set == WHITE) "1-0" else "0-1"}"
            MoveEffect.DRAW -> "  ½ - ½"
            else -> ""
        }
        return "$symbol$capture$to$postFix"
    }
}
