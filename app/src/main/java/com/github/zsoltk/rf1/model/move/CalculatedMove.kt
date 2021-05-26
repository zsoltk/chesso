package com.github.zsoltk.rf1.model.move

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Set.WHITE

data class CalculatedMove(
    val boardMove: BoardMove,
    val effect: MoveEffect? = null
) {
    val from: Position = boardMove.move.from

    val to: Position = boardMove.move.to

    val piece: Piece = boardMove.move.piece

    override fun toString(): String {
        val isCapture = boardMove.consequence is Capture
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
