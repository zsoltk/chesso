package com.github.zsoltk.chesso.model.dataviz.impl

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.dataviz.Datapoint
import com.github.zsoltk.chesso.model.dataviz.DatasetVisualisation
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import kotlinx.parcelize.Parcelize


/**
 * Calculates how many legal moves a piece can take based on the current game state.
 * Shows a colour only if the number is 0, meaning the piece is blocked from moving.
 */
@Parcelize
object BlockedPieces : DatasetVisualisation {

    override val name = R.string.viz_blocked_pieces

    override val minValue: Int = 0

    override val maxValue: Int = 31

    override fun dataPointAt(
        position: Position,
        state: GameSnapshotState,
        cache: MutableMap<Any, Any>
    ): Datapoint? =
        valueAt(position, state)?.let { value ->
            Datapoint(
                value = value,
                label = value.toString(),
                colorScale = when (value) {
                    0 -> Color.Red.copy(alpha = 0.35f) to Color.Unspecified
                    else -> Color.Unspecified to Color.Unspecified
                },
            )
        }

    private fun valueAt(position: Position, state: GameSnapshotState): Int? =
        when {
            state.board[position].isEmpty -> null
            else -> state.legalMovesFrom(position).size
        }
}
