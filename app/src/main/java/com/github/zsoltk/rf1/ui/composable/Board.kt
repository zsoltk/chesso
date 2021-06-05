package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.game.state.UiState
import com.github.zsoltk.rf1.ui.Rf1Theme
import com.github.zsoltk.rf1.ui.properties.BoardRenderProperties
import com.github.zsoltk.rf1.ui.properties.SquareRenderProperties
import com.github.zsoltk.rf1.ui.renderer.DefaultRenderer
import java.util.UUID

@Composable
fun Board(
    gamePlayState: GamePlayState,
    gameController: GameController,
    isFlipped: Boolean = false,
) {
    Board(
        fromState = gamePlayState.gameState.lastActiveState,
        toState = gamePlayState.gameState.currentSnapshotState,
        uiState = gamePlayState.uiState,
        isFlipped = isFlipped,
        onClick = { position -> gameController.onClick(position) }
    )
}

@Composable
fun Board(
    fromState: GameSnapshotState,
    toState: GameSnapshotState,
    uiState: UiState,
    isFlipped: Boolean = false,
    onClick: (Position) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val boardProperties = remember(isFlipped, maxWidth) {
            BoardRenderProperties(
                squareSize = maxWidth / 8,
                isFlipped = isFlipped
            )
        }
        Position.values().forEach { position ->
            key(position) {
                val properties = remember(uiState, isFlipped, boardProperties.squareSize) {
                    SquareRenderProperties(
                        position = position,
                        isHighlighted = position in uiState.highlightedPositions,
                        clickable = position in uiState.clickablePositions,
                        isPossibleMoveWithoutCapture = position in uiState.possibleMovesWithoutCaptures,
                        isPossibleCapture = position in uiState.possibleCaptures,
                        onClick = { onClick(position) },
                        boardProperties = boardProperties
                    )
                }
                Square(properties = properties)
            }
        }

        Pieces(
            properties = boardProperties,
            fromState = fromState,
            toState = toState
        )
    }
}

@Composable
private fun Square(
    properties: SquareRenderProperties,
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
        DefaultRenderer.decorations.forEach {
            it.render(properties)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BoardPreview() {
    Rf1Theme {
        var gamePlayState = GamePlayState()
        val gameController = GameController({ gamePlayState }, { gamePlayState = it }).apply {
            applyMove(e2, e4)
            applyMove(e7, e5)
            applyMove(b1, c3)
            applyMove(b8, c6)
            applyMove(f1, b5)
            applyMove(d7, d5)
            applyMove(e4, d5)
            applyMove(d8, d5)
            applyMove(d1, f3)
            applyMove(c8, g4)
            onClick(f3)
        }

        Board(
            gameController = gameController,
            gamePlayState = gamePlayState
        )
    }
}
