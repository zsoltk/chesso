package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.piece.Set.*

class Pawn(override val set: Set) : Piece {

    override val value: Int = 1

    override val symbol: String = when (set) {
        WHITE -> "♙"
        BLACK -> "♟︎"
    }
}
