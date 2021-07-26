package com.github.zsoltk.chesso.ui.chess.square.decoration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PositionLabel(
    text: String,
    alignment: Alignment,
    modifier: Modifier,
) {
    Box(
        contentAlignment = alignment,
        modifier = modifier.padding(start = 2.dp, end = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp
        )
    }
}
