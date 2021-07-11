package com.github.zsoltk.chesso.model.dataviz.impl

import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.dataviz.Datapoint
import com.github.zsoltk.chesso.model.dataviz.DatasetVisualisation
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import kotlinx.parcelize.Parcelize

@Parcelize
object None : DatasetVisualisation {

    override val name = R.string.viz_none

    override val minValue: Int = Int.MIN_VALUE

    override val maxValue: Int = Int.MAX_VALUE

    override fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint? =
        null
}
