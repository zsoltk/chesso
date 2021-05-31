package com.github.zsoltk.rf1.ui.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
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
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.GameController
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.game.state.UiState
import com.github.zsoltk.rf1.model.move.AppliedMove
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.ui.Rf1Theme

private enum class MoveState {
    From, To
}

@Composable
fun AnimatedBoard(
    gamePlayState: GamePlayState,
    gameController: GameController
) {
//    val currentState by remember { mutableStateOf(gameController.gameState.currentSnaphotState) }

    Board(
        gameSnapshotState = gamePlayState.gameState.prevSnapshotState ?: gamePlayState.gameState.currentSnapshotState,
//        gameSnaphotState = gamePlayState.gameState.currentSnaphotState,
        uiState = gamePlayState.uiState,
        onClick = { position -> gameController.onClick(position) },
        move = gameController.gameSnapshotState.lastMove
    )
}

@Composable
fun Board(
    gameSnapshotState: GameSnapshotState,
    uiState: UiState,
    onClick: (Position) -> Unit,
    move: AppliedMove? = null,
) {
    Board(
        fetchSquare = { position -> gameSnapshotState.boardState.board[position] },
        highlightedPositions = uiState.highlightedPositions,
        clickablePositions = uiState.clickablePositions,
        possibleMoves = uiState.possibleMovesWithoutCaptures,
        possibleCaptures = uiState.possibleCaptures,
        onClick = onClick,
        move = move,
    )
}

@Composable
fun Board(
    fetchSquare: (Position) -> Square,
    highlightedPositions: List<Position>,
    clickablePositions: List<Position>,
    possibleMoves: List<Position>,
    possibleCaptures: List<Position>,
    onClick: (Position) -> Unit,
    move: AppliedMove? = null,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            // TODO check .zIndex()
    ) {
        val squareSize = this.maxWidth / 8

        EightByEight { position ->
            Square(
                position = position,
                isHighlighted = position in highlightedPositions,
                clickable = position in clickablePositions,
                isPossibleMove = position in possibleMoves,
                isPossibleCapture = position in possibleCaptures,
                onClick = { onClick(position) },
                isDark = fetchSquare(position).isDark
            )
        }

        EightByEight { position ->
            Piece(
                piece = fetchSquare(position).piece,
                offset = pieceOffset(position, move).times(squareSize.value)
            )
        }
    }
}

@Composable
private fun pieceOffset(position: Position, move: AppliedMove?): Offset =
    if (position == move?.from) {
        Offset(
            x = (move.to.file - move.from.file) * 1f,
            y = (move.to.rank - move.from.rank) * -1f,
        )
    } else Offset.Zero

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
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
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
private fun Piece(
    piece: Piece?,
    modifier: Modifier = Modifier,
    offset: Offset? = null
) {
    piece?.let {
        val appliedOffset = if (offset != null) {
            var currentState = remember { MutableTransitionState(MoveState.From) }
            currentState.targetState = MoveState.To
            val transition = updateTransition(currentState, label = "Move progress")
            val animatedOffsetValue by transition.animateOffset(
                transitionSpec = {
                    tween(durationMillis = 100, easing = LinearEasing)
                },
                label = "Move progress"
            ) { state: MoveState ->
                when (state) {
                    MoveState.From -> Offset.Zero
                    MoveState.To -> offset
                }
            }

            animatedOffsetValue

        } else Offset.Zero

        Text(
            text = it.symbol,
            modifier = modifier.offset(Dp(appliedOffset.x), Dp(appliedOffset.y)),
            fontSize = 40.sp
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
        val gameController = GameController({ gamePlayState }, { gamePlayState = it}).apply {
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

        AnimatedBoard(
            gameController = gameController,
            gamePlayState = gamePlayState
        )
    }
}
