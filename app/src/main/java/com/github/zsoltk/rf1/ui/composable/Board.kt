package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.board.File
import com.github.zsoltk.rf1.model.board.Square
import com.github.zsoltk.rf1.model.game.GameState
import com.github.zsoltk.rf1.model.game.UiState
import com.github.zsoltk.rf1.model.notation.Position
import com.github.zsoltk.rf1.ui.Rf1Theme

@Composable
fun Board(gameState: GameState, uiState: UiState, onMove: (from: Position, to: Position) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        Column {
            for (rank in 8 downTo 1) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    for (file in 1..8) {
                        val square = gameState.board[file, rank]
                        requireNotNull(square)

                        Square(
                            file = file,
                            rank = rank,
                            gameState = gameState,
                            uiState = uiState,
                            square = square,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            onMove = onMove
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Square(
    file: Int,
    rank: Int,
    gameState: GameState,
    uiState: UiState,
    square: Square,
    modifier: Modifier,
    onMove: (from: Position, to: Position) -> Unit
) {
    val canBeSelected = gameState.toMove == square.piece?.set
    val isSelected = uiState.selectedPosition.value == square.position
    val isLastMove = square.position == gameState.lastMove?.first || square.position == gameState.lastMove?.second
    val isHighlighted = isSelected || isLastMove

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            // TODO from theme
            .background(if (square.isDark) Color.LightGray else Color.White)
            .clickable(
                enabled = canBeSelected,
                onClick = {
                    if (isSelected) {
                        uiState.selectedPosition.value = null
                    } else {
                        uiState.selectedPosition.value = square.position
                    }
                }
            )
    ) {
        if (isHighlighted) {
            HighlightSquare()
        }
        if (file == 1) {
            PositionLabel(rank.toString(), Alignment.TopStart)
        }
        if (rank == 1) {
            PositionLabel(
                File.values()[file - 1].toString(),
                Alignment.BottomEnd
            )
        }

        Piece(square)
        PossibleMoves(
            square = square,
            gameState = gameState,
            uiState = uiState,
            onMove = onMove
        )
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
private fun Piece(square: Square) {
    square.piece?.let {
        Text(
            text = it.symbol,
            fontSize = 36.sp
        )
    }
}

@Composable
private fun PossibleMoves(
    square: Square,
    gameState: GameState,
    uiState: UiState,
    onMove: (from: Position, to: Position) -> Unit
) {
    var possibleMoves = emptyList<Position>()
    val selectedPosition = uiState.selectedPosition.value

    selectedPosition?.let { position ->
        val selectedSquare = gameState.board[position]
        selectedSquare.piece?.let {
            possibleMoves = it.moves(gameState)
        }
    }

    if (square.position in possibleMoves) {
        requireNotNull(selectedPosition)

        Canvas(modifier = Modifier
            .fillMaxSize()
            .clickable {
                onMove.invoke(
                    selectedPosition,
                    square.position
                )
            }
        ) {
            val baseRadius = size.minDimension / 6f
            val radius = if (square.isEmpty) baseRadius else baseRadius * 2f
            val style = if (square.isEmpty) Fill else Stroke(width = baseRadius / 2)
            drawCircle(
                color = Color.DarkGray,
                radius = radius,
                alpha = 0.25f,
                style = style
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoardPreview() {
    Rf1Theme {
        Board(
            gameState = GameState(),
            uiState = UiState().apply {
                selectedPosition.value = Position.e2
            },
            onMove = { _, _ -> }
        )
    }
}
