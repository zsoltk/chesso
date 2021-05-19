package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.piece.Set.*

class Rook(override val set: Set) : Piece {

    override val value: Int = 5

    override val symbol: String = when (set) {
        WHITE -> "♖"
        BLACK -> "♜"
    }
}
