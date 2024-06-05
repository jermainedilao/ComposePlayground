package com.example.composeplayground

import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativeCanvas
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlin.math.roundToInt

// Reference: https://stackoverflow.com/a/71090112
// https://github.com/SmartToolFactory/Compose-Drawing-App/blob/master/app/src/main/java/com/smarttoolfactory/composedrawingapp/DrawingApp.kt
class ComposeDrawingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DrawingCanvas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun DrawingCanvas(modifier: Modifier = Modifier) {
    val path = remember { Path() }
    var lines by remember { mutableIntStateOf(0) }
    val picture = remember { Picture() }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PurpleBackgroundColor)
            .padding(vertical = 16.dp)
    ) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
                .align(Alignment.Center)
                .offset(y = (-50).dp)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            path.moveTo(it.x, it.y)
                            path.lineTo(it.x, it.y)
                            lines += 1
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            path.moveTo(it.x, it.y)
                            currentPosition = it
                            previousPosition = currentPosition
                            lines += 1
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            currentPosition = change.position

                            path.quadraticBezierTo(
                                previousPosition.x,
                                previousPosition.y,
                                (previousPosition.x + currentPosition.x) / 2,
                                (previousPosition.y + currentPosition.y) / 2
                            )
                            previousPosition = currentPosition
                            lines += 1
                        },
                        onDragEnd = {
                            previousPosition = Offset.Unspecified
                        }
                    )
                }
                .drawWithCache {
                    val width = this.size.width.roundToInt()
                    val height = this.size.height.roundToInt()
                    onDrawWithContent {
                        val pictureCanvas = picture.beginRecording(width, height)
                        if (lines > 0) {
                            pictureCanvas.drawPath(path.asAndroidPath(), NativePaint().apply {
                                color = android.graphics.Color.BLACK
                                style = android.graphics.Paint.Style.STROKE
                                strokeCap = android.graphics.Paint.Cap.ROUND
                                strokeJoin = android.graphics.Paint.Join.ROUND
                                strokeWidth = 10f
                            })
                            drawPath(path, Color.Black, style = Stroke(10f, cap = StrokeCap.Round, join = StrokeJoin.Round))
                        }
                        picture.endRecording()
                    }
                },
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .align(Alignment.BottomCenter),
            onClick = {
                val bitmap = Bitmap.createBitmap(picture.width, picture.height, Bitmap.Config.ARGB_8888)
                val canvas = NativeCanvas(bitmap)
                canvas.drawColor(android.graphics.Color.WHITE)
                canvas.drawPicture(picture)
                Log.d("ComposeDrawingActivity", "DrawingCanvas: $bitmap")
            }) {
            Text(text = "Test bitmap")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawingCanvasPreview() {
    ComposePlaygroundTheme {
        DrawingCanvas()
    }
}