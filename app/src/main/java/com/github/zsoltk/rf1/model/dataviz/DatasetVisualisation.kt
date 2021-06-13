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

    fun dataPointAt(position: Position, state: GameSnapshotState): Datapoint?
}

data class Datapoint(
    val value: Int?,
    val label: String?,
    val colorScale: Pair<Color, Color>,
)

val datasetVisualisations = listOf(
    None,
    LegalMoveCount,
    Influence,
    KnightsMoveCount,
    CheckmateCount
)

val ActiveDatasetVisualisation = compositionLocalOf<DatasetVisualisation> { None }
