package com.github.zsoltk.rf1.model.game.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GamePlayState(
    val gameState: GameState = GameState(),
    val uiState: UiState = UiState(gameState.currentSnapshotState),
    val promotionState: PromotionState = PromotionState.None
) : Parcelable
