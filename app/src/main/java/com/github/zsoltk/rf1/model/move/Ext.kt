package com.github.zsoltk.rf1.model.move

import com.github.zsoltk.rf1.model.board.Position

fun List<Move>.targetPositions(): List<Position> =
    map { it.to }
