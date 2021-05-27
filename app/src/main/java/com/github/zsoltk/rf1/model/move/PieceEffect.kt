package com.github.zsoltk.rf1.model.move

import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.piece.Piece

interface PieceEffect {

    val piece: Piece

    fun applyOn(board: Board): Board
}

interface PreMove : PieceEffect

interface PrimaryMove : PieceEffect {

    val from: Position

    val to: Position
}

interface Consequence : PieceEffect

data class Move(
    override val piece: Piece,
    override val from: Position,
    override val to: Position
) : PrimaryMove, Consequence {

    constructor(
        piece: Piece,
        intent: MoveIntention,
    ) : this(
        piece = piece,
        from = intent.from,
        to = intent.to
    )

    override fun applyOn(board: Board): Board =
        board.copy(
            pieces = board.pieces
                .minus(from)
                .plus(to to piece)
        )
}

data class Capture(
    override val piece: Piece,
    val position: Position,
) : PreMove {

    override fun applyOn(board: Board): Board =
        board.copy(
            pieces = board.pieces.minus(position)
        )
        // A move might have been a applied already, let's not remove the new piece
        else board

}

