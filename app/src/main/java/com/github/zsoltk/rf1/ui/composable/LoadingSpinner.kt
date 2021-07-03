package com.github.zsoltk.rf1.ui.composable

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.zsoltk.rf1.ui.alice_blue

@Composable
fun LoadingSpinner() {
    MaterialTheme {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            CircularProgressIndicator(
                color = alice_blue
            )
        }
    }
}
