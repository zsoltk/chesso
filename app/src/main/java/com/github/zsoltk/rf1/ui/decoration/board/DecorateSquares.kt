package com.github.zsoltk.rf1.ui.decoration.board

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.input.pointer.pointerInput
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.ui.composable.toOffset
import com.github.zsoltk.rf1.ui.decoration.square.SquareDecoration
import com.github.zsoltk.rf1.ui.properties.BoardRenderProperties
import com.github.zsoltk.rf1.ui.properties.SquareRenderProperties
import java.util.UUID

class DecorateSquares(
    private val decorations: List<SquareDecoration>,
) : BoardDecoration {

    @Composable
    override fun render(properties: BoardRenderProperties) {
        Position.values().forEach { position ->
            key(position) {
                val squareProperties = remember(properties) {
                    SquareRenderProperties(
                        position = position,
                        isHighlighted = position in properties.uiState.highlightedPositions,
                        clickable = position in properties.uiState.clickablePositions,
                        isPossibleMoveWithoutCapture = position in properties.uiState.possibleMovesWithoutCaptures,
                        isPossibleCapture = position in properties.uiState.possibleCaptures,
                        onClick = { properties.onClick(position) },
                        boardProperties = properties
                    )
                }
                Square(
                    properties = squareProperties,
                    decorations = decorations
                )
            }
        }
    }

    @Composable
    private fun Square(
        properties: SquareRenderProperties,
        decorations: List<SquareDecoration>,
    ) {
        Box(
            modifier = properties.coordinate
                .toOffset(properties.boardProperties.squareSize)
                .pointerInput(UUID.randomUUID()) {
                    detectTapGestures(
                        onPress = { if (properties.clickable) properties.onClick() },
                    )
                }
        ) {
            decorations.forEach {
                it.render(properties)
            }
        }
    }
}
