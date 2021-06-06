package com.github.zsoltk.rf1.ui.renderer.board

import androidx.compose.ui.unit.Dp
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.game.state.UiState

data class BoardRenderProperties(
    val fromState: GameSnapshotState,
    val toState: GameSnapshotState,
    val uiState: UiState,
    val isFlipped: Boolean,
    val squareSize: Dp,
    val onClick: (Position) -> Unit,
)
