package com.github.zsoltk.rf1.model.dataviz.impl

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.dataviz.Datapoint
import com.github.zsoltk.rf1.model.dataviz.DatasetVisualisation
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import kotlinx.parcelize.Parcelize


@Parcelize
object LegalMoveCount : DatasetVisualisation {

    override val name = "Legal moves of pieces"

    override val minValue: Int = 0

    override val maxValue: Int = 1

    override fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint? =
        valueAt(position, state)?.let { value ->
            Datapoint(
                value = value,
                label = value.toString(),
                colorScale = Color.Red.copy(alpha = 0.35f) to Color.Transparent,
            )
        }

    private fun valueAt(position: Position, state: GameSnapshotState): Int? =
        when {
            state.board[position].isEmpty -> null
            else -> state.legalMovesFrom(position).size
        }
}
