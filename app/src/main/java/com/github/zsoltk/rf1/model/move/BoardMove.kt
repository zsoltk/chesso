package com.github.zsoltk.rf1.model.move

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.piece.Piece

data class BoardMove(
    val move: Move,
    val consequence: EffectOnPiece? = null
) {
    val from: Position = move.from

    val to: Position = move.to

    val piece: Piece = move.piece
}
