package com.github.zsoltk.rf1.model.dataviz

import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import kotlinx.parcelize.Parcelize


@Parcelize
object LegalMoveCount : DatasetVisualisation {

    override val name = "Legal moves of pieces"
    override val minValue: Int = 0
    override val maxValue: Int = 1
    override val colorMin = Color.Red.copy(alpha = 0.35f)
    override val colorMax = Color.Transparent

    override fun valueAt(position: Position, state: GameSnapshotState): Int? =
        when {
            state.board[position].isEmpty -> null
            else -> state.legalMovesFrom(position).size
        }
}
