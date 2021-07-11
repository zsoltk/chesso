package com.github.zsoltk.chesso.ui.composable.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.zsoltk.chesso.model.game.converter.PgnConverter

@Composable
fun ImportDialog(
    onDismiss: () -> Unit,
    onImport: (String) -> Unit,
) {
    MaterialTheme {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            ImportDialogContent(
                validate = { PgnConverter.preValidate(it) },
                onCancel = onDismiss,
                onDone = onImport
            )
        }
    }
}

@Preview
@Composable
private fun ImportDialogContent(
    validate: (String) -> Boolean = { true },
    onCancel: () -> Unit = {},
    onDone: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(48.dp)
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            ),
        horizontalAlignment = Alignment.Start
    ) {
        var text by rememberSaveable { mutableStateOf("") }
        val isValid = remember(text) { validate(text) }

        TextField(
            value = text,
            onValueChange = { text = it },
            isError = !isValid,
            label = @Composable {
                Text(
                    text = "PGN",
                    fontWeight = FontWeight.Bold
                )
            },
            placeholder = @Composable {
                Text(text = "1. d4 f5 2. Bf4 e6 3. Nf3 Nf6 4. h3 Nd5 5. Bh2 Nc6 6. e3 b5")
            }
        )

        Row(
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = { onDone(text) },
                modifier = Modifier.padding(8.dp),
                enabled = isValid
            ) {
                Text(text = "Done")
            }
        }
    }
}
