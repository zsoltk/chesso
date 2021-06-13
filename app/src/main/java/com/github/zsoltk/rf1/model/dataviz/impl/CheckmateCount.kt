package com.github.zsoltk.rf1.model.dataviz.impl

import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.dataviz.Datapoint
import com.github.zsoltk.rf1.model.dataviz.DatasetVisualisation
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.ui.amaranth_red
import com.github.zsoltk.rf1.ui.silver_sand
import kotlinx.parcelize.Parcelize

/**
 * Based on the post of /u/atlas_scrubbed in /r/chess on Reddit:
 *
 * https://www.reddit.com/r/chess/comments/kp7qwe/i_looked_at_a_million_games_played_on_lichess_and
 */
@Parcelize
object CheckmateCount : DatasetVisualisation {

    override val name: String = "Checkmates on a given square"

    override val minValue: Int = 465

    override val maxValue: Int = 26745

    override fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint {
        val value = valueAt(position)
        return Datapoint(
            value = value,
            label = value.toString(),
            colorScale = silver_sand to amaranth_red,
        )
    }

    private fun valueAt(position: Position): Int =
        when(position) {
            a1 -> 3458
            b1 -> 4367
            c1 -> 5860
            d1 -> 4875
            e1 -> 9066
            f1 -> 9545
            g1 -> 24937
            h1 -> 17034

            a2 -> 1916
            b2 -> 465
            c2 -> 699
            d2 -> 1150
            e2 -> 1667
            f2 -> 1211
            g2 -> 1560
            h2 -> 6016

            a3 -> 2620
            b3 -> 688
            c3 -> 842
            d3 -> 1246
            e3 -> 1394
            f3 -> 1632
            g3 -> 1539
            h3 -> 5531

            a4 -> 3011
            b4 -> 676
            c4 -> 859
            d4 -> 848
            e4 -> 1148
            f4 -> 1716
            g4 -> 1848
            h4 -> 5629

            a5 -> 3084
            b5 -> 671
            c5 -> 862
            d5 -> 839
            e5 -> 1140
            f5 -> 1587
            g5 -> 1823
            h5 -> 5538

            a6 -> 2847
            b6 -> 787
            c6 -> 1066
            d6 -> 1427
            e6 -> 1430
            f6 -> 1890
            g6 -> 1533
            h6 -> 5607

            a7 -> 1995
            b7 -> 504
            c7 -> 856
            d7 -> 1526
            e7 -> 2650
            f7 -> 1647
            g7 -> 2066
            h7 -> 5990

            a8 -> 3653
            b8 -> 4714
            c8 -> 6654
            d8 -> 5756
            e8 -> 14753
            f8 -> 10901
            g8 -> 26745
            h8 -> 17271
        }
}

