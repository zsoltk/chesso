package com.github.zsoltk.rf1.model.dataviz

import androidx.compose.runtime.compositionLocalOf
import com.github.zsoltk.rf1.model.dataviz.impl.CheckmateCount
import com.github.zsoltk.rf1.model.dataviz.impl.Influence
import com.github.zsoltk.rf1.model.dataviz.impl.KnightsMoveCount
import com.github.zsoltk.rf1.model.dataviz.impl.LegalMoveCount
import com.github.zsoltk.rf1.model.dataviz.impl.None

val datasetVisualisations = listOf(
    None,
    LegalMoveCount,
    Influence,
    KnightsMoveCount,
    CheckmateCount
)

val ActiveDatasetVisualisation = compositionLocalOf<DatasetVisualisation> { None }
