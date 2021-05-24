package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece

data class Move(
    val from: Position,
    val to: Position,
    val piece: Piece,
    val isCapture: Boolean? = null,
    val isCheck: Boolean? = null,
    val isCheckMate: Boolean? = null
) {

    override fun toString(): String {
        val symbol = when {
            piece !is Pawn -> piece.symbol
            isCapture == true -> from.fileAsLetter
            else -> ""
        }
        val capture = if (isCapture == true) "x" else ""
        val postFix = if (isCheckMate == true) "#" else if (isCheck == true) "+" else ""
        return "$symbol$capture$to$postFix"
    }
}
