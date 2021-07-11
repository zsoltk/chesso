package com.github.zsoltk.chesso.ui.renderer.board

import androidx.compose.runtime.Composable

interface BoardDecoration {

    @Composable
    fun render(properties: BoardRenderProperties)
}
