package com.github.zsoltk.rf1.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.github.zsoltk.rf1.model.game.converter.PgnConverter
import com.github.zsoltk.rf1.model.game.state.GamePlayState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ManagedImport(
    pgnToImport: MutableState<String?>,
    gamePlayState: MutableState<GamePlayState>,
) {
    val pgn = pgnToImport.value

    if (pgn != null) {
        LoadingSpinner()
        LaunchedEffect(pgn) {
            withContext(Dispatchers.IO) {
                val importedState = PgnConverter.import(pgn)
                val importedGame = GamePlayState(importedState)
                gamePlayState.value = importedGame

                pgnToImport.value = null
            }
        }
    }
}
