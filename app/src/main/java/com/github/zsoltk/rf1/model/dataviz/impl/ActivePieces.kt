package com.github.zsoltk.rf1.model.dataviz.impl

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.R
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.dataviz.Datapoint
import com.github.zsoltk.rf1.model.dataviz.DatasetVisualisation
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import kotlinx.parcelize.Parcelize


/**
 * Calculates how many legal moves a piece can take based on the current game state.
 * Shows a proportionally stronger colour the larger this humber is.
 */
@Parcelize
object ActivePieces : DatasetVisualisation {

    override val name = R.string.viz_active_pieces

    override val minValue: Int = 2

    override val maxValue: Int = 10

    override fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint? =
        valueAt(position, state)?.let { value ->
            Datapoint(
                value = value,
                label = value.toString(),
                colorScale = Color.Green.copy(alpha = 0.025f) to Color.Green.copy(alpha = 0.85f)
            )
        }

    private fun valueAt(position: Position, state: GameSnapshotState): Int? =
        when {
            state.board[position].isEmpty -> null
            else -> state.legalMovesFrom(position).size
        }
}
