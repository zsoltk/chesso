package com.github.zsoltk.rf1.ui.decoration.square

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.github.zsoltk.rf1.ui.properties.SquareRenderProperties
import java.util.UUID

object TargetMarks : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        if (properties.isPossibleMoveWithoutCapture) {
            PossibleMoveWithoutCapture(
                onClick = properties.onClick,
                modifier = properties.sizeModifier
            )
        } else if (properties.isPossibleCapture) {
            PossibleCapture(
                onClick = properties.onClick,
                modifier = properties.sizeModifier
            )
        }
    }

    @Composable
    private fun PossibleMoveWithoutCapture(
        onClick: () -> Unit,
        modifier: Modifier,
    ) {
        CircleDecoratedSquare(
            onClick = onClick,
            radius = { size.minDimension / 6f },
            drawStyle = { Fill },
            modifier = modifier
        )
    }

    @Composable
    private fun PossibleCapture(
        onClick: () -> Unit,
        modifier: Modifier,
    ) {
        CircleDecoratedSquare(
            onClick = onClick,
            radius = { size.minDimension / 3f },
            drawStyle = { Stroke(width = size.minDimension / 12f) },
            modifier = modifier
        )
    }

    @Composable
    private fun CircleDecoratedSquare(
        onClick: () -> Unit,
        radius: DrawScope.() -> Float,
        drawStyle: DrawScope.() -> DrawStyle,
        modifier: Modifier,
    ) {
        Canvas(
            modifier = modifier
                .pointerInput(UUID.randomUUID()) {
                    detectTapGestures(
                        onPress = { onClick() },
                    )
                }
        ) {
            drawCircle(
                color = Color.DarkGray,
                radius = radius(this),
                alpha = 0.25f,
                style = drawStyle(this)
            )
        }
    }
}
