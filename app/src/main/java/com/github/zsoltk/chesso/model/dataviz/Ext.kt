package com.github.zsoltk.chesso.model.dataviz

import androidx.compose.runtime.compositionLocalOf
import com.github.zsoltk.chesso.model.dataviz.impl.ActivePieces
import com.github.zsoltk.chesso.model.dataviz.impl.CheckmateCount
import com.github.zsoltk.chesso.model.dataviz.impl.Influence
import com.github.zsoltk.chesso.model.dataviz.impl.KnightsMoveCount
import com.github.zsoltk.chesso.model.dataviz.impl.BlockedPieces
import com.github.zsoltk.chesso.model.dataviz.impl.None

val datasetVisualisations = listOf(
    None,
    ActivePieces,
    BlockedPieces,
    Influence,
    KnightsMoveCount,
    CheckmateCount
)

val ActiveDatasetVisualisation = compositionLocalOf<DatasetVisualisation> { None }
