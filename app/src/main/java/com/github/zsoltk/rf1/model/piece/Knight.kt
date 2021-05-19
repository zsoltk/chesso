package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.piece.Set.*

class Knight(override val set: Set) : Piece {

    override val value: Int = 3

    override val symbol: String = when (set) {
        WHITE -> "♘"
        BLACK -> "♞"
    }
}
