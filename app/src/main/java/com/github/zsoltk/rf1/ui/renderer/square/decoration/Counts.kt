package com.github.zsoltk.rf1.ui.renderer.square.decoration

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.board.Position
import com.github.zsoltk.rf1.ui.renderer.square.SquareDecoration
import com.github.zsoltk.rf1.ui.renderer.square.SquareRenderProperties

/**
 * Based on the post of /u/atlas_scrubbed in /r/chess on Reddit:
 *
 * https://www.reddit.com/r/chess/comments/kp7qwe/i_looked_at_a_million_games_played_on_lichess_and
 */
abstract class Counts(
    private val colorMin: Color,
    private val colorMax: Color,
) : SquareDecoration {

    private val counts: List<Int> = Position.values().map { countAt(it) }
    private val min = counts.minOrNull()!!
    private val max = counts.maxOrNull()!!

    @Composable
    final override fun render(properties: SquareRenderProperties) {
        val count = countAt(properties.position)
        val percentage = (1.0f * count - min) / (max - min)
        val progress = remember { Animatable(0f) }

        // TODO later with compositionLocal, so values can be updated by selected overlay
        // TODO w/o LaunchedEffect
        LaunchedEffect(properties) {
            progress.animateTo(
                targetValue = percentage,
                animationSpec = tween(5000),
            )
        }
        
        val currentColor = Color(
            red = colorMin.red + progress.value * (colorMax.red - colorMin.red),
            green = colorMin.green + progress.value * (colorMax.green - colorMin.green),
            blue = colorMin.blue + progress.value * (colorMax.blue - colorMin.blue),
            alpha = colorMin.alpha + progress.value * (colorMax.alpha - colorMin.alpha),
        )

        Box(
            modifier = properties.sizeModifier
                .background(currentColor),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = count.toString(),
                fontSize = 10.sp
            )
        }
    }

    abstract fun countAt(position: Position): Int
}

