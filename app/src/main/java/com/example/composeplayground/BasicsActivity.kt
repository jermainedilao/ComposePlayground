package com.example.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme

class BasicsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Basics(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun Basics(modifier: Modifier = Modifier) {
    val dogImage = ImageBitmap.imageResource(id = R.drawable.dog)
    Column(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasQuadrantSize = size / 4f
            drawRect(
                color = Color.Magenta,
                size = canvasQuadrantSize,
            )
            drawLine(
                color = Color.Blue,
                start = Offset(size.width, 0f),
                end = Offset(0f, size.height),
            )
            drawImage(dogImage, topLeft = Offset(size.width / 2 - dogImage.width / 2, size.height / 2))

        }
    }
}

@Preview
@Composable
private fun BasicsPreview(modifier: Modifier = Modifier) {
    ComposePlaygroundTheme {
        Basics()
    }
}