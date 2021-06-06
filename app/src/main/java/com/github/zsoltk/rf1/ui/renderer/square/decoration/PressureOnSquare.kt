package com.github.zsoltk.rf1.ui.renderer.square.decoration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.github.zsoltk.rf1.ui.renderer.square.SquareRenderProperties
import com.github.zsoltk.rf1.ui.renderer.square.SquareDecoration


/**
 * Experimental only.
 *
 * Currently not implemented but would need to be taken into account:
 * - pressure != legal move (defended piece blocks the square for other moves until taken)
 * - lined up defending pieces (e.g. rook + queen) also block each other, but in a sequence of moves that's not a problem
 * - only calculates opponent's pressure, but pressure difference is also important
 *
 * For efficiency, the required calculations should be moved to board level too.
 */
object PressureOnSquare: SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        val pressure = remember(properties.boardProperties.toState) { properties.calculatePressure() }
        if (pressure == 0) return

        PositionLabel(
            text = pressure.toString(),
            alignment = Alignment.TopEnd,
            modifier = properties.sizeModifier
        )
    }

    private fun SquareRenderProperties.calculatePressure(): Int {
        val toState = boardProperties.toState
        val boardState = toState.boardState
        val toMove = boardState.toMove
        val board = boardState.board
        val opponentPieces = board.pieces(toMove.opposite())
        val pressure = opponentPieces
            // TODO calculate moves only once per board, not once per square
            .flatMap { (_, piece) -> piece.pressure(toState, false) }
            .filter { it.move.to == position }
            .size

        return pressure
    }
}
