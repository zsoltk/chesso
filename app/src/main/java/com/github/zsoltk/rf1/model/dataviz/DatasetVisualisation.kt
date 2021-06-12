package com.github.zsoltk.rf1.model.dataviz

import android.os.Parcelable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.model.board.Position

interface DatasetVisualisation : Parcelable {

    val name: String

    val minValue: Int

    val maxValue: Int

    val colorMin: Color

    val colorMax: Color

    fun valueAt(position: Position): Int?
}

val datasetVisualisations = listOf(
    None,
    KnightsMoveCount,
    CheckmateCount
)

val ActiveDatasetVisualisation = compositionLocalOf<DatasetVisualisation> { None }
