package com.github.zsoltk.rf1.model.move

import com.github.zsoltk.rf1.model.notation.Position

data class MoveIntention(
    val from: Position,
    val to: Position
)
