package com.github.zsoltk.chesso.ui.chess.board

import androidx.compose.ui.unit.Dp
import com.github.zsoltk.chesso.model.board.Position
import com.github.zsoltk.chesso.model.game.state.GameSnapshotState
import com.github.zsoltk.chesso.model.game.state.UiState

data class BoardRenderProperties(
    val fromState: GameSnapshotState,
    val toState: GameSnapshotState,
    val uiState: UiState,
    val isFlipped: Boolean,
    val squareSize: Dp,
    val onClick: (Position) -> Unit,
) {
    val cache: MutableMap<Any, Any> = mutableMapOf()
}
