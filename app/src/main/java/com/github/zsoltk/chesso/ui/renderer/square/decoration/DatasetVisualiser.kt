package com.github.zsoltk.chesso.ui.renderer.square.decoration

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
import com.github.zsoltk.chesso.model.dataviz.ActiveDatasetVisualisation
import com.github.zsoltk.chesso.ui.renderer.square.SquareDecoration
import com.github.zsoltk.chesso.ui.renderer.square.SquareRenderProperties

/**
 * Based on the post of /u/atlas_scrubbed in /r/chess on Reddit:
 *
 * https://www.reddit.com/r/chess/comments/kp7qwe/i_looked_at_a_million_games_played_on_lichess_and
 */
object DatasetVisualiser : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        ActiveDatasetVisualisation.current.let { viz ->
            val datapoint = viz.dataPointAt(
                properties.position,
                properties.boardProperties.toState,
                properties.boardProperties.cache
            )

            val percentage = datapoint?.value?.let {
                (1.0f * datapoint.value - viz.minValue) / (viz.maxValue - viz.minValue)
            }?.coerceIn(0f, 1f)

            val intermediateColor = percentage?.let {
                val colorMin = datapoint.colorScale.first
                val colorMax = datapoint.colorScale.second
                Color(
                    red = colorMin.red + percentage * (colorMax.red - colorMin.red),
                    green = colorMin.green + percentage * (colorMax.green - colorMin.green),
                    blue = colorMin.blue + percentage * (colorMax.blue - colorMin.blue),
                    alpha = colorMin.alpha + percentage * (colorMax.alpha - colorMin.alpha),
                )
            }

            val color by animateColorAsState(
                targetValue = intermediateColor ?: Color.Transparent,
                animationSpec = tween(1500)
            )

            datapoint?.let {
                Box(
                    modifier = properties.sizeModifier
                        .background(color),
                    contentAlignment = Alignment.TopEnd
                ) {
                    datapoint.label?.let {
                        Text(
                            text = datapoint.label,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

