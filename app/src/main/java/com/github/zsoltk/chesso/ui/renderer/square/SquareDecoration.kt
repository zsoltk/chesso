package com.github.zsoltk.chesso.ui.renderer.square

import androidx.compose.runtime.Composable

interface SquareDecoration {

    @Composable
    fun render(properties: SquareRenderProperties)
}

