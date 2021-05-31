package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.state.GameSnaphotState
import com.github.zsoltk.rf1.model.piece.Bishop
import com.github.zsoltk.rf1.model.piece.Knight
import com.github.zsoltk.rf1.model.piece.Pawn
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.BLACK
import com.github.zsoltk.rf1.model.piece.Set.WHITE
import com.github.zsoltk.rf1.ui.Rf1Theme
import kotlin.math.absoluteValue

@Composable
fun CapturedPieces(gameState: GameState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(MaterialTheme.colors.secondaryVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val capturedPieces = gameState.currentSnaphotState.capturedPieces
                .sortedWith { t1, t2 ->
                    if (t1.value == t2.value) t1.symbol.hashCode() - t2.symbol.hashCode()
                    else t1.value - t2.value
                }

            val score = gameState.currentSnaphotState.score
            CapturedPieceList(capturedPieces, capturedBy = WHITE, score)
            CapturedPieceList(capturedPieces, capturedBy = BLACK, score)
        }
    }
}

@Composable
private fun CapturedPieceList(capturedPieces: List<Piece>, capturedBy: Set, score: Int) {
    val stringBuilder = StringBuilder()
    capturedPieces
        .filter { it.set == capturedBy.opposite() }
        .forEach { stringBuilder.append(it.symbol) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (capturedBy == BLACK && score < 0) {
            Score(score)
        }
        Text(
            text = stringBuilder.toString(),
            color = MaterialTheme.colors.onSecondary,
            fontSize = 20.sp
        )
        if (capturedBy == WHITE && score > 0) {
            Score(score)
        }
    }
}

@Composable
private fun Score(score: Int) {
    Text(
        text = "+${score.absoluteValue}",
        color = MaterialTheme.colors.onSecondary,
        fontSize = 12.sp,
        modifier = Modifier.padding(
            all = 8.dp
        ),
    )
}

@Preview
@Composable
fun TakenPiecesPreview() {
    Rf1Theme {
        CapturedPieces(
            gameState = GameState(
                states = listOf(
                    GameSnaphotState(
                        capturedPieces = listOf(
                            Pawn(WHITE),
                            Pawn(WHITE),
                            Pawn(WHITE),
                            Pawn(WHITE),
                            Knight(WHITE),
                            Knight(WHITE),
                            Bishop(WHITE),
                            Queen(WHITE),

                            Pawn(BLACK),
                            Pawn(BLACK),
                            Pawn(BLACK),
                            Pawn(BLACK),
                            Knight(BLACK),
                            Rook(BLACK),
                        )
                    )
                )
            )
        )
    }
}
