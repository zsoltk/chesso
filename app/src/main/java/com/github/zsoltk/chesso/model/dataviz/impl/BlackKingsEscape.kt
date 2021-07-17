package com.github.zsoltk.chesso.model.dataviz.impl

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.piece.Set
import kotlinx.parcelize.Parcelize

@Parcelize
object BlackKingsEscape : KingsEscapeSquares(
    set = Set.BLACK,
    colorScale = Color.Unspecified to Color(0xBBEE6666)
) {
    override val name = R.string.viz_black_kings_escape_squares
}
