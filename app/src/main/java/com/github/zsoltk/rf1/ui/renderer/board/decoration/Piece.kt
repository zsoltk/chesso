package com.github.zsoltk.rf1.ui.renderer.board.decoration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.piece.Bishop
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Rook

@Composable
fun Piece(
    piece: Piece,
    squareSize: Dp,
    modifier: Modifier = Modifier
) {
    key(piece) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.size(squareSize, squareSize)
        ) {
            piece.asset?.let {
                Icon(
                    painter = painterResource(id = it),
                    tint = Color.Unspecified,
                    contentDescription = "${piece.set} ${piece.javaClass.simpleName}"
                )
            } ?: run {
                Text(
                    text = piece.symbol,
                    color = Color.Black,
                    fontSize = when (piece) {
                        // TODO should accommodate non-board rendering
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
