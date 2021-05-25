package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Piece

data class Move(
    val from: Position,
    val to: Position,
    val piece: Piece,
)
