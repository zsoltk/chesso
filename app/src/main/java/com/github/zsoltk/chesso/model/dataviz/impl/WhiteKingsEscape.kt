package com.github.zsoltk.chesso.model.dataviz.impl

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.piece.Set
import kotlinx.parcelize.Parcelize

@Parcelize
object WhiteKingsEscape : KingsEscapeSquares(
    set = Set.WHITE,
    colorScale = Color.Unspecified to Color(0xBB6666EE)
) {
    override val name = R.string.viz_white_kings_escape_squares
}
