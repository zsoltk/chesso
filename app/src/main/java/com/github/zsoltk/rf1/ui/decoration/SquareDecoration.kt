package com.github.zsoltk.rf1.ui.decoration

import androidx.compose.runtime.Composable
import com.github.zsoltk.rf1.ui.properties.SquareRenderProperties

interface SquareDecoration {

    @Composable
    fun render(properties: SquareRenderProperties)
}

