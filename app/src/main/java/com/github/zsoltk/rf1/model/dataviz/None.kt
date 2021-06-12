package com.github.zsoltk.rf1.model.dataviz

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import kotlinx.parcelize.Parcelize

@Parcelize
object None : DatasetVisualisation {

    override val name = "None"

    override val minValue: Int = Int.MIN_VALUE

    override val maxValue: Int = Int.MAX_VALUE

    override val colorMin = Color.Transparent

    override val colorMax = Color.Transparent

    override fun valueAt(position: Position, state: GameSnapshotState): Int? =
        null
}
