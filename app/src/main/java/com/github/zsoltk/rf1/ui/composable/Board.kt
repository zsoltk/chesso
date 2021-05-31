package com.github.zsoltk.rf1.ui.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.game.GameController
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.game.state.UiState
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.ui.Rf1Theme

@Composable
fun Board(
    gamePlayState: GamePlayState,
    gameController: GameController
) {
    Board(
        fromState = gamePlayState.gameState.lastActiveState,
        toState = gamePlayState.gameState.currentSnapshotState,
        uiState = gamePlayState.uiState,
        onClick = { position -> gameController.onClick(position) }
    )
}

@Composable
fun Board(
    fromState: GameSnapshotState,
    toState: GameSnapshotState,
    uiState: UiState,
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

        EightByEight { position ->
            Square(
                position = position,
                isHighlighted = position in uiState.highlightedPositions,
                clickable = position in uiState.clickablePositions,
                isPossibleMove = position in uiState.possibleMovesWithoutCaptures,
                isPossibleCapture = position in uiState.possibleCaptures,
                onClick = { onClick(position) },
                isDark = fetchSquare(position).isDark
            )
        }

        Pieces(fromState, toState, squareSize)
    }
}

@Composable
private fun Pieces(
    fromState: GameSnapshotState,
    toState: GameSnapshotState,
    squareSize: Dp
) {
    val progress = remember(toState) { Animatable(0f) }
    LaunchedEffect(toState) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(80, easing = LinearEasing),
        )
    }

    toState.board.pieces.forEach { (toPosition, piece) ->
        val offset1 = Offset(
            x = (toPosition.file - 1) * 1f,
            y = (8 - toPosition.rank) * 1f,
        )

        val fromPosition = fromState.board.find(piece)?.position
        if (fromPosition == null) {
            Piece(
                piece = piece,
                squareSize = squareSize,
                squareOffset = offset1
            )

        } else {
            val offset0 = Offset(
                x = (fromPosition.file - 1) * 1f,
                y = (8 - fromPosition.rank) * 1f,
            )

            val currentOffset = offset0 + (offset1 - offset0).times(progress.value)
            Piece(
                piece = piece,
                squareSize = squareSize,
                squareOffset = currentOffset
            )

        }
    }
}


@Composable
private fun Piece(
    piece: Piece,
    squareSize: Dp,
    squareOffset: Offset
) {
    val dpOffset = squareOffset.times(squareSize.value)

    key(piece) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(squareSize, squareSize)
        ) {
            Text(
                text = piece.symbol,
                modifier = Modifier.offset(Dp(dpOffset.x), Dp(dpOffset.y)),
                fontSize = 40.sp
            )
        }
    }
}

@Composable
private fun EightByEight(
    content: @Composable (Position) -> Unit
) {
    Column {
        for (rank in 8 downTo 1) {
            Row(Modifier.weight(1f)) {
                for (file in 1..8) {
                    InSquare(Modifier.weight(1f)) {
                        content(Position.from(file, rank))
                    }
                }
            }
        }
    }
}

@Composable
private fun InSquare(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        content()
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
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .clickable(
                enabled = clickable,
                onClick = onClick
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(if (isDark) Color.LightGray else Color.White)
        }

        if (isHighlighted) {
            HighlightSquare()
        }
        if (position.file == 1) {
            PositionLabel(position.rank.toString(), Alignment.TopStart)
        }
        if (position.rank == 1) {
            PositionLabel(
                position.fileAsLetter.toString(),
                Alignment.BottomEnd
            )
        }

        if (isPossibleMove) {
            PossibleMove(onClick)
        } else if (isPossibleCapture) {
            PossibleCapture(onClick)
        }
    }
}

@Composable
private fun HighlightSquare() {
    Canvas(modifier = Modifier.fillMaxSize()) {
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
    alignment: Alignment
) {
    Box(
        contentAlignment = alignment,
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 2.dp)
    ) {
        // TODO text colour = inverse of square colour
        Text(
            text = text,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun PossibleMove(
    onClick: () -> Unit
) {
    CircleDecoratedSquare(
        onClick = onClick,
        radius = { size.minDimension / 6f },
        drawStyle = { Fill }
    )
}

@Composable
private fun PossibleCapture(
    onClick: () -> Unit
) {
    CircleDecoratedSquare(
        onClick = onClick,
        radius = { size.minDimension / 3f },
        drawStyle = { Stroke(width = size.minDimension / 12f) }
    )
}

@Composable
private fun CircleDecoratedSquare(
    onClick: () -> Unit,
    radius: DrawScope.() -> Float,
    drawStyle: DrawScope.() -> DrawStyle
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
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
