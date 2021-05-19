package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.board.Board
import com.github.zsoltk.rf1.model.board.File
import com.github.zsoltk.rf1.ui.Rf1Theme

@Composable
fun Board(board: Board) {
    Column {
        for (rank in 8 downTo 1) {
            Row {
                for (file in 1..8) {
                    val square = board[file, rank]

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(36.dp)
                            .height(36.dp)
                            // TODO from theme
                            .background(if (square.isDark) Color.LightGray else Color.White)
                    ) {
                        if (file == 1) {
                            PositionLabel(rank.toString(), Alignment.TopStart)
                        }
                        if (rank == 1) {
                            PositionLabel(File.values()[file - 1].toString(), Alignment.BottomEnd)
                        }

                        square.piece?.let {
                            Text(
                                text = it.symbol,
                                fontSize = 28.sp
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun PositionLabel(
    text: String,
    alignment: Alignment
) {
    Box(
        contentAlignment = alignment,
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(end = 2.dp)
    ) {
        // TODO text colour = inverse of square colour
        Text(
            text = text,
            fontSize = 10.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Rf1Theme {
        Board(Board())
    }
}
