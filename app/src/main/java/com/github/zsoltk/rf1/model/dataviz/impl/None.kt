package com.github.zsoltk.rf1.model.dataviz.impl

import com.github.zsoltk.rf1.R
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.dataviz.Datapoint
import com.github.zsoltk.rf1.model.dataviz.DatasetVisualisation
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import kotlinx.parcelize.Parcelize

@Parcelize
object None : DatasetVisualisation {

    override val name = R.string.viz_none

    override val minValue: Int = Int.MIN_VALUE

    override val maxValue: Int = Int.MAX_VALUE

    override fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint? =
        null
}
