package com.github.zsoltk.rf1.model.game.state

import android.os.Parcelable
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.piece.Piece
import kotlinx.parcelize.Parcelize

sealed class PromotionState : Parcelable {
    @Parcelize
    object None : PromotionState()

    @Parcelize
    data class Await(val position: Position) : PromotionState()

    @Parcelize
    data class ContinueWith(val piece: Piece) : PromotionState()
}
