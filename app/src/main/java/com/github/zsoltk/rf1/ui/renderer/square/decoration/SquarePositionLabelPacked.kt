package com.github.zsoltk.rf1.ui.renderer.square.decoration

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.github.zsoltk.rf1.model.board.Coordinate
import com.github.zsoltk.rf1.ui.renderer.square.SquareRenderProperties
import com.github.zsoltk.rf1.ui.renderer.square.SquareDecoration

open class SquarePositionLabelPacked(
    private val display: (Coordinate) -> Boolean,
) : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        if (display(properties.coordinate)) {
            PositionLabel(
                text = properties.position.toString(),
                alignment = Alignment.TopStart,
                modifier = properties.sizeModifier
            )
        }
    }
}
