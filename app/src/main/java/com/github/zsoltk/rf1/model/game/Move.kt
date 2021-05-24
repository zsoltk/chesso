package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece

data class Move(
    val from: Position,
    val to: Position,
    val piece: Piece,
    val isCapture: Boolean,
    val isCheck: Boolean
) {

    override fun toString(): String {
        val symbol = when {
            piece !is Pawn -> piece.symbol
            isCapture -> from.fileAsLetter
            else -> ""
        }
        val capture = if (isCapture) "x" else ""
        val isCheck = if (isCheck) "+" else ""
        return "$symbol$capture$to$isCheck"
    }
}
