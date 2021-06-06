package com.github.zsoltk.rf1.ui.decoration.board

import androidx.compose.runtime.Composable
import com.github.zsoltk.rf1.ui.properties.BoardRenderProperties

interface BoardDecoration {

    @Composable
    fun render(properties: BoardRenderProperties)
}
