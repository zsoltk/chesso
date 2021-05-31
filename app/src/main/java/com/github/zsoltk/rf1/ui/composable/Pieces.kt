package com.github.zsoltk.rf1.ui.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.game.state.GameSnapshotState
import com.github.zsoltk.rf1.model.piece.Piece

@Composable
fun Pieces(
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
fun Piece(
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

@Preview
@Composable
fun PiecesPreview() {
    BoardPreview()
}
