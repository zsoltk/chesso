package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.zsoltk.rf1.model.piece.Bishop
import com.github.zsoltk.rf1.model.piece.Knight
import com.github.zsoltk.rf1.model.piece.Piece
import com.github.zsoltk.rf1.model.piece.Queen
import com.github.zsoltk.rf1.model.piece.Rook
import com.github.zsoltk.rf1.model.piece.Set
import com.github.zsoltk.rf1.model.piece.Set.WHITE

@Composable
fun PromotionDialog(
    set: Set = WHITE,
    onPieceSelected: (Piece) -> Unit,
) {
    MaterialTheme {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            PromotionDialogContent(set) {
                onPieceSelected(it)
            }
        }
    }
}

@Preview
@Composable
private fun PromotionDialogContent(
    set: Set = WHITE,
    onClick: (Piece) -> Unit = {}
) {
    val promotionPieces = listOf(
        Queen(set),
        Rook(set),
        Bishop(set),
        Knight(set)
    )

    Column(
        modifier = Modifier.background(
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.medium
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        promotionPieces.forEach { piece ->
            Text(
                text = piece.symbol,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(onClick = { onClick(piece) })
                ,
                color = MaterialTheme.colors.onSurface,
                fontSize = 40.sp
            )
        }
    }
}
