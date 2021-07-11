package com.github.zsoltk.chesso.model.dataviz

import androidx.compose.ui.graphics.Color

data class Datapoint(
    val value: Int?,
    val label: String?,
    val colorScale: Pair<Color, Color>,
)
