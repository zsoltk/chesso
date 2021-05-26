package com.github.zsoltk.rf1.model.move

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.piece.Piece

sealed class EffectOnPiece {
    abstract val piece: Piece
}

data class Move(
    override val piece: Piece,
    val intent: MoveIntention,
) : EffectOnPiece() {
    constructor(
        piece: Piece,
        from: Position,
        to: Position
    ) : this(
        intent = MoveIntention(from, to),
        piece = piece
    )

    val from: Position = intent.from

    val to: Position = intent.to

}

data class Capture(
    override val piece: Piece,
    val position: Position,
) : EffectOnPiece()

