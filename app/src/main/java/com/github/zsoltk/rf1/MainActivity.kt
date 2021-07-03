package com.github.zsoltk.rf1

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zsoltk.rf1.ui.Rf1Theme
import com.github.zsoltk.rf1.ui.composable.Game

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val importGameText = when {
            intent?.action == Intent.ACTION_SEND && "text/plain" == intent.type -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)
            }
            else -> null
        }
        setContent {
            Rf1Theme {
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
    Rf1Theme {
        Game()
    }
}
