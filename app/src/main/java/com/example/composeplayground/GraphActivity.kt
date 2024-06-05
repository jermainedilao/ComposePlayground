package com.example.composeplayground

import android.graphics.PointF
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme
import java.math.BigDecimal
import java.time.LocalDate

// References:
// https://github.com/riggaroo/compose-playtime/blob/main/app/src/main/java/dev/riggaroo/composeplaytime/SmoothLineGraph.kt
// https://www.youtube.com/watch?v=1yiuxWK74vI
class GraphActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Graph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Graph(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(PurpleBackgroundColor)
            .fillMaxSize()
    ) {
        DrawBackground(modifier = Modifier.align(Alignment.Center))
        DrawPath(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun DrawPath(modifier: Modifier) {
    val animationProgress = if (LocalInspectionMode.current) {
        remember { Animatable(1f) }
    } else {
        remember { Animatable(0f) }
    }
    LaunchedEffect(key1 = graphData) {
        animationProgress.animateTo(1f, tween(2000))
    }
    Spacer(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(3 / 2f)
            .fillMaxSize()
            .drawWithCache {
                val path = generatePath(size)
                val brush = Brush.verticalGradient(
                    listOf(
                        Color.Green.copy(alpha = 0.4f),
                        Color.Transparent,
                    )
                )
                val filledPath = Path().apply {
                    addPath(path)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                onDrawBehind {
                    clipRect(right = size.width * animationProgress.value) {
                        drawPath(path, Color.Green, style = Stroke(2.dp.toPx()))
                        drawPath(filledPath, brush, style = Fill)
                    }
                }
            }
    )
}

@Composable
private fun DrawBackground(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(3 / 2f)
            .fillMaxSize()
    ) {
        val barWidthPx = 1.dp.toPx()
        drawRect(BarColor, style = Stroke(barWidthPx))

        val verticalLines = 4
        val verticalSize = size.width / (verticalLines + 1)
        repeat(verticalLines) { i ->
            val startX = verticalSize * (i + 1)
            drawLine(
                BarColor,
                start = Offset(startX, 0f),
                end = Offset(startX, size.height),
                strokeWidth = barWidthPx,
            )
        }

        val horizontalLines = 3
        val horizontalSize = size.height / (horizontalLines + 1)
        repeat(horizontalLines) { i ->
            val startY = horizontalSize * (i + 1)
            drawLine(
                BarColor,
                start = Offset(0f, startY),
                end = Offset(size.width, startY),
                strokeWidth = barWidthPx,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GraphPreview() {
    ComposePlaygroundTheme {
        Graph()
    }
}

fun generatePath(size: Size): Path {
    val path = Path()
    val numberEntries = graphData.size - 1
    val weekWidth = size.width / numberEntries
    val max = graphData.maxBy { it.amount }
    val min = graphData.minBy { it.amount }
    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    var previousBalanceX = 0f
    var previousBalanceY = size.height
    graphData.forEachIndexed { index, balance ->
        if (index == 0) {
            path.moveTo(
                0f,
                size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount
            )
        }
        val balanceX = index * weekWidth
        val balanceY = size.height - (balance.amount - min.amount).toFloat() * heightPxPerAmount
        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)

        path.cubicTo(
            controlPoint1.x,
            controlPoint1.y,
            controlPoint2.x,
            controlPoint2.y,
            balanceX,
            balanceY,
        )

        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return path
}

val graphData = listOf(
    Balance(LocalDate.now(), BigDecimal(65631)),
    Balance(LocalDate.now().plusWeeks(1), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(2), BigDecimal(65851)),
    Balance(LocalDate.now().plusWeeks(3), BigDecimal(65931)),
    Balance(LocalDate.now().plusWeeks(4), BigDecimal(66484)),
    Balance(LocalDate.now().plusWeeks(5), BigDecimal(67684)),
    Balance(LocalDate.now().plusWeeks(6), BigDecimal(66684)),
    Balance(LocalDate.now().plusWeeks(7), BigDecimal(66984)),
    Balance(LocalDate.now().plusWeeks(8), BigDecimal(70600)),
    Balance(LocalDate.now().plusWeeks(9), BigDecimal(71600)),
    Balance(LocalDate.now().plusWeeks(10), BigDecimal(72600)),
    Balance(LocalDate.now().plusWeeks(11), BigDecimal(72526)),
    Balance(LocalDate.now().plusWeeks(12), BigDecimal(72976)),
    Balance(LocalDate.now().plusWeeks(13), BigDecimal(73589)),
)
val PurpleBackgroundColor = Color(0xff322049)
val BarColor = Color.White.copy(alpha = 0.3f)

data class Balance(val date: LocalDate, val amount: BigDecimal)