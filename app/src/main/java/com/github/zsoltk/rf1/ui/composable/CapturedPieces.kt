package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.game.Game
import com.github.zsoltk.rf1.model.game.GameState
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
fun CapturedPieces(game: Game) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colors.secondaryVariant),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val capturedPieces = game.currentState.capturedPieces
            .sortedWith { t1, t2 ->
                if (t1.value == t2.value) t1.symbol.hashCode() - t2.symbol.hashCode()
                else t1.value - t2.value
            }

        val score = game.currentState.score
        CapturedPieceList(capturedPieces, WHITE, score)
        CapturedPieceList(capturedPieces, BLACK, score)
    }
}

@Composable
private fun CapturedPieceList(capturedPieces: List<Piece>, set: Set, score: Int) {
    val stringBuilder = StringBuilder()
    if (set == BLACK && score < 0) stringBuilder.append("(+${score.absoluteValue}) ")
    capturedPieces
        .filter { it.set == set }
        .forEach { stringBuilder.append(it.symbol) }

    if (set == WHITE && score > 0) stringBuilder.append(" (+$score)")

    Text(
        text = stringBuilder.toString(),
        Modifier.padding(16.dp),
        color = MaterialTheme.colors.onSecondary,
        fontSize = 20.sp
    )
}

@Preview
@Composable
fun TakenPiecesPreview() {
    Rf1Theme {
        CapturedPieces(
            game = Game().apply {
                states.add(
                    GameState(
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
            }
        )
    }
}
