package com.github.zsoltk.rf1.model.piece

import com.github.zsoltk.rf1.model.game.CurrentState
import com.github.zsoltk.rf1.model.notation.Position

interface Piece {

    val set: Set

    val symbol: String

    val value: Int

    fun moves(currentState: CurrentState): List<Position> = emptyList()
}
