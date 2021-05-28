package com.github.zsoltk.rf1.model.move

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece

data class BoardMove(
    val move: PrimaryMove,
    val preMove: PreMove? = null,
    val consequence: Consequence? = null
) {
    val from: Position = move.from

    val to: Position = move.to

    val piece: Piece = move.piece

    override fun toString(): String {
        if (move is KingSideCastle) return "O-O"
        if (move is QueenSideCastle) return "O-O-O"
        val isCapture = preMove is Capture
        val symbol = when {
            piece !is Pawn -> piece.symbol
            isCapture -> from.fileAsLetter
            else -> ""
        }
        val capture = if (isCapture) "x" else ""
        val promotion = if (consequence is Promotion) "=${consequence.piece.textSymbol}" else ""

        return "$symbol$capture$to$promotion"
    }
}
