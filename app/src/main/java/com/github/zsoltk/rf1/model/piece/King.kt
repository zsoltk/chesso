package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.piece.Set.*

class King(override val set: Set) : Piece {

    override val value: Int = Int.MAX_VALUE

    override val symbol: String = when (set) {
        WHITE -> "♔"
        BLACK -> "♚"
    }
}
