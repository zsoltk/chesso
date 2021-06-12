package com.github.zsoltk.rf1.model.dataviz

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.model.board.Position
import kotlinx.parcelize.Parcelize

/**
 * Based on the post of /u/IconicIsotope in /r/chess on Reddit:
 *
 * https://www.reddit.com/r/chess/comments/nij28s/knight_moves_a_simple_table_i_made_showing_the
 */
@Parcelize
object None : DatasetVisualisation {

    override val name = "None"

    override val minValue: Int = Int.MIN_VALUE

    override val maxValue: Int = Int.MAX_VALUE

    override val colorMin = Color.Transparent

    override val colorMax = Color.Transparent

    override fun valueAt(position: Position): Int? =
        null

}
