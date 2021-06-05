package com.github.zsoltk.rf1.ui.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.board.toCoordinate
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.piece.Bishop
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Rook

@Composable
fun Pieces(
    fromState: GameSnapshotState,
    toState: GameSnapshotState,
    squareSize: Dp,
    isFlipped: Boolean,
) {
    val progress = remember(toState) { Animatable(0f) }
    LaunchedEffect(toState) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(80, easing = LinearEasing),
        )
    }

    toState.board.pieces.forEach { (toPosition, piece) ->
        val offset1 = toPosition.toCoordinate(isFlipped)
        val fromPosition = fromState.board.find(piece)?.position
        if (fromPosition == null) {
            Piece(
                piece = piece,
                squareSize = squareSize,
                modifier = offset1.toOffset(squareSize)
            )

        } else {
            val offset0 = fromPosition.toCoordinate(isFlipped)
            val currentOffset = offset0 + (offset1 - offset0).times(progress.value)
            Piece(
                piece = piece,
                squareSize = squareSize,
                modifier = currentOffset.toOffset(squareSize)
            )
        }
    }
}


@Composable
fun Piece(
    piece: Piece,
    squareSize: Dp,
    modifier: Modifier
) {
    key(piece) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(squareSize, squareSize)
        ) {
            Text(
                text = piece.symbol,
                color = Color.Black,
                modifier = modifier,
                fontSize = when (piece) {
                    is Pawn -> 36.sp
                    is Bishop -> 41.sp
                    is Rook -> 41.sp
                    else -> 40.sp
                }
            )
        }
    }
}

@Preview
@Composable
fun PiecesPreview() {
    BoardPreview()
}
