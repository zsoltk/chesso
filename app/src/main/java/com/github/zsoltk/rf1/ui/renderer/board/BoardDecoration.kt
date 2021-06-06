package com.github.zsoltk.rf1.ui.renderer.board

import androidx.compose.runtime.Composable

interface BoardDecoration {

    @Composable
    fun render(properties: BoardRenderProperties)
}
