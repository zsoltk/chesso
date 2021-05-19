package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.piece.Set.*

class Queen(override val set: Set) : Piece {

    override val value: Int = 9

    override val symbol: String = when (set) {
        WHITE -> "♕"
        BLACK -> "♛"
    }
}
