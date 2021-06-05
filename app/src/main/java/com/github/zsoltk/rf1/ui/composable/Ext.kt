package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.github.zsoltk.rf1.model.board.Coordinate

fun Coordinate.toOffset(squareSize: Dp): Modifier =
    Modifier
        .offset(
            Dp((x - 1) * squareSize.value),
            Dp((y - 1) * squareSize.value)
        )
