package com.github.zsoltk.rf1.model.dataviz

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.piece.Set
import kotlinx.parcelize.Parcelize


@Parcelize
object Influence : DatasetVisualisation {

    override val name = "Influence (simplified)"
    override val minValue: Int = -5
    override val maxValue: Int = 5

    private val redScale = Color.Red.copy(alpha = 0.5f) to Color.Transparent
    private val blueScale = Color.Transparent to Color.Blue.copy(alpha = 0.5f)

    override fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint? {
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
