package com.github.zsoltk.rf1.ui.renderer.square

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import com.github.zsoltk.rf1.model.board.Coordinate
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.board.toCoordinate
import com.github.zsoltk.rf1.ui.renderer.board.BoardRenderProperties

data class SquareRenderProperties(
    val position: Position,
    val isHighlighted: Boolean,
    val clickable: Boolean,
    val onClick: () -> Unit,
    val isPossibleMoveWithoutCapture: Boolean,
    val isPossibleCapture: Boolean,
    val boardProperties: BoardRenderProperties
) {
    val coordinate: Coordinate =
        position.toCoordinate(boardProperties.isFlipped)

    val sizeModifier: Modifier
        get() = Modifier.size(boardProperties.squareSize)
}
