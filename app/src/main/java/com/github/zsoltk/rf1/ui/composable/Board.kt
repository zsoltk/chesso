package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.board.Coordinate
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.board.toCoordinate
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.game.state.UiState
import com.github.zsoltk.rf1.ui.Rf1Theme
import java.util.UUID

@Composable
fun Board(
    gamePlayState: GamePlayState,
    gameController: GameController,
    isFlipped: Boolean = false
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
    onClick: (Position) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val squareSize = this.maxWidth / 8
        val fetchSquare = { position: Position ->
            fromState.boardState.board[position]
        }

        Position.values().forEach { position ->
            Square(
                position = position,
                isHighlighted = position in uiState.highlightedPositions,
                clickable = position in uiState.clickablePositions,
                isPossibleMove = position in uiState.possibleMovesWithoutCaptures,
                isPossibleCapture = position in uiState.possibleCaptures,
                onClick = { onClick(position) },
                isDark = fetchSquare(position).isDark,
                coordinate = position.toCoordinate(isFlipped),
                squareSize = squareSize
            )
        }

        Pieces(
            fromState = fromState,
            toState = toState,
            squareSize = squareSize,
            isFlipped = isFlipped
        )
    }
}

@Composable
private fun Square(
    position: Position,
    isHighlighted: Boolean,
    clickable: Boolean,
    onClick: () -> Unit,
    isPossibleMove: Boolean,
    isPossibleCapture: Boolean,
    isDark: Boolean,
    coordinate: Coordinate,
    squareSize: Dp,
) {
    val size = Modifier.size(squareSize)

    Box(
        modifier = coordinate
            .toOffset(squareSize)
            .pointerInput(UUID.randomUUID()) {
                detectTapGestures(
                    onPress = { if (clickable) onClick() },
                )
            }
    ) {
        Canvas(size) {
            drawRect(if (isDark) Color.LightGray else Color.White)
        }

        if (isHighlighted) {
            HighlightSquare(size)
        }
        if (coordinate.x == Coordinate.min.x) {
            PositionLabel(
                text = position.rank.toString(),
                alignment = Alignment.TopStart,
                modifier = size
            )
        }
        if (coordinate.y == Coordinate.max.y) {
            PositionLabel(
                text = position.fileAsLetter.toString(),
                alignment = Alignment.BottomEnd,
                modifier = size
            )
        }

        if (isPossibleMove) {
            PossibleMove(onClick, size)
        } else if (isPossibleCapture) {
            PossibleCapture(onClick, size)
        }
    }
}

@Composable
private fun HighlightSquare(
    modifier: Modifier,
) {
    Canvas(modifier = modifier) {
        drawRect(
            color = Color.Yellow,
            size = size,
            alpha = 0.15f
        )
    }
}

@Composable
private fun PositionLabel(
    text: String,
    alignment: Alignment,
    modifier: Modifier,
) {
    Box(
        contentAlignment = alignment,
        modifier = modifier.padding(end = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun PossibleMove(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    CircleDecoratedSquare(
        onClick = onClick,
        radius = { size.minDimension / 6f },
        drawStyle = { Fill },
        modifier = modifier
    )
}

@Composable
private fun PossibleCapture(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    CircleDecoratedSquare(
        onClick = onClick,
        radius = { size.minDimension / 3f },
        drawStyle = { Stroke(width = size.minDimension / 12f) },
        modifier = modifier
    )
}

@Composable
private fun CircleDecoratedSquare(
    onClick: () -> Unit,
    radius: DrawScope.() -> Float,
    drawStyle: DrawScope.() -> DrawStyle,
    modifier: Modifier,
) {
    Canvas(
        modifier = modifier
            .pointerInput(UUID.randomUUID()) {
                detectTapGestures(
                    onPress = { onClick() },
                )
            }
    ) {
        drawCircle(
            color = Color.DarkGray,
            radius = radius(this),
            alpha = 0.25f,
            style = drawStyle(this)
        )
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
