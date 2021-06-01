package com.github.zsoltk.rf1.model.game.state

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.piece.Piece

sealed class PromotionState {
    object None : PromotionState()
    data class Await(val position: Position) : PromotionState()
    data class ContinueWith(val piece: Piece) : PromotionState()
}
