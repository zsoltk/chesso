package com.github.zsoltk.chesso.model.move

import com.github.zsoltk.chesso.model.board.Position

data class MoveIntention(
    val from: Position,
    val to: Position
)
