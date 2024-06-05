package com.example.composeplayground

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme
import com.example.composeplayground.ui.theme.PurpleBackgroundColor

@Composable
fun BasicsScreen() {
    val dogImage = ImageBitmap.imageResource(id = R.drawable.dog)
    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
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
        BasicsScreen()
    }
}