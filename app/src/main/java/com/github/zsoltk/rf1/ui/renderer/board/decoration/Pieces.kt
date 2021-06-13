package com.github.zsoltk.rf1.ui.renderer.board.decoration

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
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
import androidx.compose.ui.geometry.Offset
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
import com.github.zsoltk.rf1.ui.renderer.board.BoardDecoration
import com.github.zsoltk.rf1.ui.renderer.board.BoardRenderProperties

object Pieces : BoardDecoration {

    @Composable
    override fun render(properties: BoardRenderProperties) {
        properties.toState.board.pieces.forEach { (toPosition, piece) ->
            key(piece) {
                val fromPosition = properties.fromState.board.find(piece)?.position
                val currentOffset = fromPosition
                    ?.toCoordinate(properties.isFlipped)
                    ?.toOffset(properties.squareSize)

                val targetOffset = toPosition
                    .toCoordinate(properties.isFlipped)
                    .toOffset(properties.squareSize)

                val offset = remember { Animatable(currentOffset ?: targetOffset, Offset.VectorConverter) }
                LaunchedEffect(targetOffset) {
                    offset.animateTo(targetOffset, tween(100, easing = LinearEasing))
                }
                LaunchedEffect(properties.isFlipped) {
                    offset.snapTo(targetOffset)
                }

                Piece(
                    piece = piece,
                    squareSize = properties.squareSize,
                    modifier = offset.value.toModifier()
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
