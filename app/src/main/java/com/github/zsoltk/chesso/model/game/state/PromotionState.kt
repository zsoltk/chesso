package com.github.zsoltk.chesso.model.game.state

import android.os.Parcelable
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.piece.Piece
import kotlinx.parcelize.Parcelize

sealed class PromotionState : Parcelable {
    @Parcelize
    object None : PromotionState()

    @Parcelize
    data class Await(val position: Position) : PromotionState()

    @Parcelize
    data class ContinueWith(val piece: Piece) : PromotionState()
}
