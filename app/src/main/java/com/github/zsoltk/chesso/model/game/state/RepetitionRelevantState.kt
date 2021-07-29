package com.github.zsoltk.chesso.model.game.state

import android.os.Parcelable
import com.github.zsoltk.chesso.model.board.Board
import com.github.zsoltk.chesso.model.piece.Set
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepetitionRelevantState(
    val board: Board,
    val toMove: Set,
    val castlingInfo: CastlingInfo,
) : Parcelable
