package com.github.zsoltk.chesso

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.chesso.ui.ChessoTheme
import com.github.zsoltk.chesso.ui.composable.Game

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val importGameText = when {
            savedInstanceState == null && intent?.action == Intent.ACTION_SEND && "text/plain" == intent.type -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)
            }
            else -> null
        }
        setContent {
            ChessoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Game(importGameText = importGameText)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    ChessoTheme {
        Game()
    }
}
