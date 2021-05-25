package com.github.zsoltk.rf1.model.game

import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.model.piece.Piece

data class Move(
    val intent: MoveIntention,
    val piece: Piece,
) {
    constructor(
        from: Position,
        to: Position,
        piece: Piece
    ) : this(
        intent = MoveIntention(from, to),
        piece = piece
    )

    val from: Position = intent.from

    val to: Position = intent.to
}
