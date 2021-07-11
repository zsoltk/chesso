package com.github.zsoltk.chesso.model.move

import com.github.zsoltk.chesso.model.board.Position

fun List<BoardMove>.targetPositions(): List<Position> =
    map { it.to }
