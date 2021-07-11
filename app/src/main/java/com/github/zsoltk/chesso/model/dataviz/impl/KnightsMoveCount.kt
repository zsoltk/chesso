package com.github.zsoltk.chesso.model.dataviz.impl

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.board.Position.*
import com.github.zsoltk.chesso.model.dataviz.Datapoint
import com.github.zsoltk.chesso.model.dataviz.DatasetVisualisation
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import kotlinx.parcelize.Parcelize

/**
 * Based on the post of /u/IconicIsotope in /r/chess on Reddit:
 *
 * https://www.reddit.com/r/chess/comments/nij28s/knight_moves_a_simple_table_i_made_showing_the
 */
@Parcelize
object KnightsMoveCount : DatasetVisualisation {

    override val name = R.string.viz_knight_move_count

    override val minValue: Int = 2

    override val maxValue: Int = 8

    override fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint {
        val value = valueAt(position)
        return Datapoint(
            value = value,
            label = value.toString(),
            colorScale = Color.DarkGray to Color.Green
        )
    }

    private fun valueAt(position: Position): Int =
        when(position) {
            a1 -> 2
            b1 -> 3
            c1 -> 4
            d1 -> 4
            e1 -> 4
            f1 -> 4
            g1 -> 3
            h1 -> 2

            a2 -> 3
            b2 -> 4
            c2 -> 6
            d2 -> 6
            e2 -> 6
            f2 -> 6
            g2 -> 4
            h2 -> 3

            a3 -> 4
            b3 -> 6
            c3 -> 8
            d3 -> 8
            e3 -> 8
            f3 -> 8
            g3 -> 6
            h3 -> 4

            a4 -> 4
            b4 -> 6
            c4 -> 8
            d4 -> 8
            e4 -> 8
            f4 -> 8
            g4 -> 6
            h4 -> 4

            a5 -> 4
            b5 -> 6
            c5 -> 8
            d5 -> 8
            e5 -> 8
            f5 -> 8
            g5 -> 6
            h5 -> 4

            a6 -> 4
            b6 -> 6
            c6 -> 8
            d6 -> 8
            e6 -> 8
            f6 -> 8
            g6 -> 6
            h6 -> 4

            a7 -> 3
            b7 -> 4
            c7 -> 6
            d7 -> 6
            e7 -> 6
            f7 -> 6
            g7 -> 4
            h7 -> 3

            a8 -> 2
            b8 -> 3
            c8 -> 4
            d8 -> 4
            e8 -> 4
            f8 -> 4
            g8 -> 3
            h8 -> 2
        }
}
