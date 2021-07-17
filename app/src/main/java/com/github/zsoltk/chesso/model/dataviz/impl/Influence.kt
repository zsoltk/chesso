package com.github.zsoltk.chesso.model.dataviz.impl

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.chesso.R
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.dataviz.Datapoint
import com.github.zsoltk.chesso.model.dataviz.DatasetVisualisation
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.piece.Set
import kotlinx.parcelize.Parcelize

/**
 * Calculates simplified influence based on current game state:
 * - Calculates how many pieces can move to a square
 * - Uses different colour scale for dominating side
 * - Does not take into account defenders, as they're blocked by the piece being defended
 */
@Parcelize
object Influence : DatasetVisualisation {

    override val name = R.string.viz_influence_simplified

    override val minValue: Int = -5

    override val maxValue: Int = 5

    private val redScale = Color.Red.copy(alpha = 0.5f) to Color.Transparent

    private val blueScale = Color.Transparent to Color.Blue.copy(alpha = 0.5f)

    override fun dataPointAt(
        position: Position,
        state: GameSnapshotState,
        cache: MutableMap<Any, Any>
    ): Datapoint? {
        val square = state.board[position]
        val legalMovesTo = state.legalMovesTo(position)
        if (legalMovesTo.isEmpty() && square.isNotEmpty) {
            return Datapoint(
                value = if (square.hasPiece(Set.WHITE)) 1 else -1,
                label = null,
                colorScale = if (square.hasPiece(Set.WHITE)) blueScale else redScale,
            )
        }

        val sum = legalMovesTo
            .map { if (it.piece.set == Set.WHITE) 1 else -1 }
            .sum()

        return if (sum != 0) Datapoint(
            value = sum,
            label = sum.toString(),
            colorScale = if (sum > 0) blueScale else redScale,
        ) else null
    }
}
