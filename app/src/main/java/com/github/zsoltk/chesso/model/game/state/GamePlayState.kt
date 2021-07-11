package com.github.zsoltk.chesso.model.game.state

import android.os.Parcelable
import com.github.zsoltk.chesso.model.dataviz.DatasetVisualisation
import com.github.zsoltk.chesso.model.dataviz.impl.None
import kotlinx.parcelize.Parcelize

@Parcelize
data class GamePlayState(
    val gameState: GameState = GameState(GameMetaInfo.createWithDefaults()),
    val uiState: UiState = UiState(gameState.currentSnapshotState),
    val promotionState: PromotionState = PromotionState.None,
    val visualisation: DatasetVisualisation = None
) : Parcelable
