package com.github.zsoltk.rf1.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.rf1.model.board.Position.*
import com.github.zsoltk.rf1.model.dataviz.ActiveDatasetVisualisation
import com.github.zsoltk.rf1.model.game.state.GameState
import com.github.zsoltk.rf1.model.game.controller.GameController
import com.github.zsoltk.rf1.model.game.Resolution
import com.github.zsoltk.rf1.model.game.converter.PgnConverter
import com.github.zsoltk.rf1.model.game.preset.CheckMateTest
import com.github.zsoltk.rf1.model.game.preset.Preset
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import com.github.zsoltk.rf1.ui.Rf1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Game(state: GamePlayState = GamePlayState(), preset: Preset? = null) {
    var isFlipped by rememberSaveable { mutableStateOf(false) }
    var gamePlayState by rememberSaveable { mutableStateOf(state) }
    var showVizDialog by remember { mutableStateOf(false) }
    val gameController = remember {
        GameController(
            getGamePlayState = { gamePlayState },
            setGamePlayState = { gamePlayState = it },
            preset = preset
        )
    }

    LaunchedEffect(state) {
        withContext(Dispatchers.IO) {
            async {
                val test =
                    "[Event \"Vs. Computer\"]\n" +
                        "[Site \"Chess.com\"]\n" +
                        "[Date \"2021-06-08\"]\n" +
                        "[White \"Player1\"]\n" +
                        "[Black \"Player1\"]\n" +
                        "[Result \"1-0\"]\n" +
                        "[Termination \"Player1 won by checkmate\"]\n" +
                        "1. d4 f5 2. \nBf4 e6 3. Nf3 Nf6 4. h3 Nd5 5. Bh2 Nc6 6. e3 b5 7. Bxb5 a6 8. Bxc6 dxc6 9. Ne5 Qg5 10. O-O Rb8 11. b3 Nxe3 12. fxe3 Rb6 13. Bf4 Qe7 14. Qf3 g6 15. Nxc6 Qd7 16. Ne5 Qd8 17. Nd2 Bb7 18. Qe2 Rb4 19. Ndc4 Be4 20. c3 Rb5 21. Nd2 Bg7 22. Nxe4 fxe4 23. c4 Rb6 24. Qc2 O-O 25. Qxe4 g5 26. Bxg5 Qxg5 27. Nd7 Rf5 28. Nxb6 Qg6 29. Qa8+ Kf7 30. Rxf5+ exf5 31. Nd5 Qe6 32. Rf1 Bf6 33. Qb7 Bd8 34. Rf3 h5 35. Nxc7 Bxc7 36. Qxc7+ Kf6 37. d5 Qe5 38. Qc6+ Kg5 39. Qe6 Qxe6 40. dxe6 Kf6 41. Rxf5+ Ke7 42. Re5 h4 43. c5 Kd8 44. e7+ Ke8 45. c6 a5 46. c7 Kd7 47. e8=Q+ Kxc7 48. Qe7+ Kb6 49. Qc5+ Kb7 50. Re7+ Kb8 51. " +
                        "Qc7+ Ka8 52. Qa7# 1-0"
                gamePlayState = GamePlayState(PgnConverter.import(test))
            }.await()
        }
    }

    CompositionLocalProvider(ActiveDatasetVisualisation provides gamePlayState.visualisation) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Status(gamePlayState.gameState)
            Moves(gamePlayState.gameState, onClickMove = { gameController.goToMove(it) })
            CapturedPieces(gamePlayState.gameState)
            Board(
                gamePlayState = gamePlayState,
                gameController = gameController,
                isFlipped = isFlipped
            )
            GameControls(
                gamePlayState = gamePlayState,
                onStepBack = { gameController.stepBackward() },
                onStepForward = { gameController.stepForward() },
                onVizClicked = { showVizDialog = true },
                onFlipBoard = { isFlipped = !isFlipped },
                onNewGame = { gameController.reset() }
            )
        }

        if (gamePlayState.uiState.showPromotionDialog) {
            PromotionDialog(gameController.toMove) {
                gameController.onPromotionPieceSelected(it)
            }
        }
        if (showVizDialog) {
            PickActiveVisualisationDialog(
                onDismiss = {
                    showVizDialog = false
                },
                onItemSelected = {
                    showVizDialog = false
                    gameController.setVisualisation(it)
                }
            )
        }
    }
}

@Composable
private fun Status(gameState: GameState) {
    val text = when (gameState.resolution) {
        Resolution.IN_PROGRESS -> "${gameState.toMove} to move"
        else -> gameState.resolution.toString().replace("_", " ")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
private fun GameControls(
    gamePlayState: GamePlayState,
    onStepBack: () -> Unit,
    onStepForward: () -> Unit,
    onVizClicked: () -> Unit,
    onFlipBoard: () -> Unit,
    onNewGame: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        Button(
            onClick = onStepBack,
            enabled = gamePlayState.gameState.hasPrevIndex
        ) {
            Text("<")
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onStepForward,
            enabled = gamePlayState.gameState.hasNextIndex
        ) {
            Text(">")
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onVizClicked,
        ) {
            Text("Viz")
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onFlipBoard,
        ) {
            Text("Flip")
        }
        Spacer(Modifier.size(4.dp))
        Button(
            enabled = gamePlayState.gameState.states.size > 1,
            onClick = {
//                Log.d("PGN", PgnConverter.export(gamePlayState.gameState))
                PgnConverter.import("")
//                Log.d("PGN", PgnConverter.import("").toString())
          },
        ) {
            Text("PGN")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    Rf1Theme {
        var gamePlayState = GamePlayState()
        GameController({ gamePlayState }, { gamePlayState = it }).apply {
            applyMove(e2, e4)
            applyMove(e7, e5)
            applyMove(b1, c3)
            applyMove(b8, c6)
            applyMove(f1, b5)
            applyMove(d7, d5)
            applyMove(e4, d5)
            applyMove(d8, d5)
            applyMove(c3, d5)
            onClick(g8)
        }
        Game(
            state = gamePlayState,
        )
    }
}
