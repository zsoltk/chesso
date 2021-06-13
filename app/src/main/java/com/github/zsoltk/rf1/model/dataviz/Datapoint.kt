package com.github.zsoltk.rf1.model.dataviz

import androidx.compose.ui.graphics.Color

data class Datapoint(
    val value: Int?,
    val label: String?,
    val colorScale: Pair<Color, Color>,
)
