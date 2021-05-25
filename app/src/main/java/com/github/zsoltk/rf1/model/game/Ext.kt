package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.move.Move
import com.github.zsoltk.rf1.model.notation.Position

fun List<Move>.targetPositions(): List<Position> =
    map { it.to }
