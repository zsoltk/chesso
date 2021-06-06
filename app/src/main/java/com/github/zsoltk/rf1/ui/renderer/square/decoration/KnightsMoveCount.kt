package com.github.zsoltk.rf1.ui.renderer.square.decoration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.ui.amaranth_red
import com.github.zsoltk.rf1.ui.atomic_tangerine
import com.github.zsoltk.rf1.ui.black_coral
import com.github.zsoltk.rf1.ui.renderer.square.SquareRenderProperties
import com.github.zsoltk.rf1.ui.renderer.square.SquareDecoration
import com.github.zsoltk.rf1.ui.silver_sand
import com.github.zsoltk.rf1.ui.sizzling_red

object KnightsMoveCount : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        val moveCount = moveCount(properties.position)

        Box(
            modifier = properties.sizeModifier
                .background(moveCount.toColor().copy(alpha = 1f))
                .alpha(1f)
            ,
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = moveCount.toString(),
                fontSize = 18.sp
            )
        }
    }

    private fun Int.toColor(): Color =
        when(this) {
            2 -> amaranth_red
            3 -> sizzling_red
            4 -> atomic_tangerine
            6 -> black_coral
            8 -> silver_sand
            else -> Color.DarkGray
        }

    private fun moveCount(position: Position): Int =
        when(position) {
            a1 -> 2
            b1 -> 3
            c1 -> 4
            d1 -> 4
            e1 -> 4
            f1 -> 4
            g1 -> 3
            h1 -> 2

            a2 -> 3
            b2 -> 4
            c2 -> 6
            d2 -> 6
            e2 -> 6
            f2 -> 6
            g2 -> 4
            h2 -> 3

            a3 -> 4
            b3 -> 6
            c3 -> 8
            d3 -> 8
            e3 -> 8
            f3 -> 8
            g3 -> 6
            h3 -> 4

            a4 -> 4
            b4 -> 6
            c4 -> 8
            d4 -> 8
            e4 -> 8
            f4 -> 8
            g4 -> 6
            h4 -> 4

            a5 -> 4
            b5 -> 6
            c5 -> 8
            d5 -> 8
            e5 -> 8
            f5 -> 8
            g5 -> 6
            h5 -> 4

            a6 -> 4
            b6 -> 6
            c6 -> 8
            d6 -> 8
            e6 -> 8
            f6 -> 8
            g6 -> 6
            h6 -> 4

            a7 -> 3
            b7 -> 4
            c7 -> 6
            d7 -> 6
            e7 -> 6
            f7 -> 6
            g7 -> 4
            h7 -> 3

            a8 -> 2
            b8 -> 3
            c8 -> 4
            d8 -> 4
            e8 -> 4
            f8 -> 4
            g8 -> 3
            h8 -> 2
        }
}

