package com.github.zsoltk.rf1.ui.decoration

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.zsoltk.rf1.ui.properties.SquareRenderProperties

open class HighlightSquare(
    private val color: Color,
    private val alpha: Float,
) : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        if (properties.isHighlighted) {
            Canvas(properties.sizeModifier) {
                drawRect(
                    color = color,
                    size = size,
                    alpha = alpha
                )
            }
        }
    }
}

