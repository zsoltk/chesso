package com.github.zsoltk.rf1.ui.renderer.board.decoration

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.board.toCoordinate
import com.github.zsoltk.rf1.model.piece.Bishop
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.ui.composable.BoardPreview
import com.github.zsoltk.rf1.ui.composable.toModifier
import com.github.zsoltk.rf1.ui.composable.toOffset
import com.github.zsoltk.rf1.ui.renderer.board.BoardRenderProperties
import com.github.zsoltk.rf1.ui.renderer.board.BoardDecoration

object Pieces : BoardDecoration {

    @Composable
    override fun render(properties: BoardRenderProperties) {
        properties.toState.board.pieces.forEach { (toPosition, piece) ->
            key(piece) {
                val targetCoordinate = toPosition.toCoordinate(properties.isFlipped)
                val targetOffset = targetCoordinate.toOffset(properties.squareSize)
                val offset by animateOffsetAsState(
                    targetValue = targetOffset,
                    animationSpec = tween(100, easing = LinearEasing)
                )

                Piece(
                    piece = piece,
                    squareSize = properties.squareSize,
                    modifier = offset.toModifier()
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
}

@Preview
@Composable
fun PiecesPreview() {
    BoardPreview()
}
