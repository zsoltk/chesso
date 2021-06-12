package com.github.zsoltk.rf1.model.dataviz

import android.os.Parcelable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState

interface DatasetVisualisation : Parcelable {

    val name: String

    val minValue: Int

    val maxValue: Int

    val colorMin: Color

    val colorMax: Color

    fun valueAt(position: Position, state: GameSnapshotState): Int?
}

val datasetVisualisations = listOf(
    None,
    KnightsMoveCount,
    CheckmateCount,
    LegalMoveCount
)

val ActiveDatasetVisualisation = compositionLocalOf<DatasetVisualisation> { None }
