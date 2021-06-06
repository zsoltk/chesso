package com.github.zsoltk.rf1.ui.renderer.square

import androidx.compose.runtime.Composable

interface SquareDecoration {

    @Composable
    fun render(properties: SquareRenderProperties)
}

