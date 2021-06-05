package com.github.zsoltk.rf1.ui.decoration

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.model.board.isDark
import com.github.zsoltk.rf1.ui.properties.SquareRenderProperties

open class SquareBackground(
    private val lightSquareColor: Color,
    private val darkSquareColor: Color,
) : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        Canvas(properties.sizeModifier) {
            drawRect(color = if (properties.position.isDark()) darkSquareColor else lightSquareColor)
        }
    }
}

