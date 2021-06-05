package com.github.zsoltk.rf1.ui.decoration

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.github.zsoltk.rf1.model.board.Coordinate
import com.github.zsoltk.rf1.ui.properties.SquareRenderProperties

open class SquarePositionLabelSplit(
    private val displayFile: (Coordinate) -> Boolean,
    private val displayRank: (Coordinate) -> Boolean,
) : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        if (displayFile(properties.coordinate)) {
            PositionLabel(
                text = properties.position.fileAsLetter.toString(),
                alignment = Alignment.BottomEnd,
                modifier = properties.sizeModifier
            )
        }
        if (displayRank(properties.coordinate)) {
            PositionLabel(
                text = properties.position.rank.toString(),
                alignment = Alignment.TopStart,
                modifier = properties.sizeModifier
            )
        }
    }
}

