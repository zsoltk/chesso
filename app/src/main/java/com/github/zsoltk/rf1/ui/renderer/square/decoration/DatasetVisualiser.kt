package com.github.zsoltk.rf1.ui.renderer.square.decoration

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.github.zsoltk.rf1.model.dataviz.ActiveDatasetVisualisation
import com.github.zsoltk.rf1.ui.renderer.square.SquareDecoration
import com.github.zsoltk.rf1.ui.renderer.square.SquareRenderProperties

/**
 * Based on the post of /u/atlas_scrubbed in /r/chess on Reddit:
 *
 * https://www.reddit.com/r/chess/comments/kp7qwe/i_looked_at_a_million_games_played_on_lichess_and
 */
object DatasetVisualiser : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        ActiveDatasetVisualisation.current.let { viz ->
            val count = viz.valueAt(properties.position, properties.boardProperties.toState)
            val percentage = count?.let {
                (1.0f * count - viz.minValue) / (viz.maxValue - viz.minValue)
            }?.coerceIn(0f, 1f)

            val intermediateColor = percentage?.let {
                Color(
                    red = viz.colorMin.red + percentage * (viz.colorMax.red - viz.colorMin.red),
                    green = viz.colorMin.green + percentage * (viz.colorMax.green - viz.colorMin.green),
                    blue = viz.colorMin.blue + percentage * (viz.colorMax.blue - viz.colorMin.blue),
                    alpha = viz.colorMin.alpha + percentage * (viz.colorMax.alpha - viz.colorMin.alpha),
                )
            }

            val color by animateColorAsState(
                targetValue = intermediateColor ?: Color.Transparent,
                animationSpec = tween(2500)
            )

            count?.let {
                Box(
                    modifier = properties.sizeModifier
                        .background(color),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Text(
                        text = count.toString(),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

