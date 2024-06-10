@file:OptIn(ExperimentalFoundationApi::class)

package com.example.composeplayground

import android.util.Log
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FillInTheBlanksScreenV3() {
    Scaffold { paddingValues ->
        var targetX by remember { mutableFloatStateOf(0f) }
        var targetY by remember { mutableFloatStateOf(0f) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp, 50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
                    .onGloballyPositioned {
                        targetX = it.positionInWindow().x
                        targetY = it.positionInWindow().y
                    }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CardOption("Hola", targetX, targetY)
                CardOption("Hello", targetX, targetY)
                CardOption("Ciao", targetX, targetY)
            }
        }
    }
}

@Composable
private fun CardOption(
    text: String,
    targetX: Float,
    targetY: Float,
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    var selectedState by remember { mutableStateOf(false) }
    var startX by remember { mutableFloatStateOf(0f) }
    var startY by remember { mutableFloatStateOf(0f) }
    val xOffset by remember(targetX, startX) {
        mutableFloatStateOf(
            if (targetX < startX) {
                -(startX - targetX)
            } else {
                targetX - startX
            } + with(density) { 4.dp.toPx() }
        )
    }
    val yOffset by remember(targetY, startY) {
        mutableFloatStateOf(-(startY - targetY - with(density) { 2.dp.toPx() }))
    }
    var dragX by remember { mutableFloatStateOf(0f) }
    var dragY by remember { mutableFloatStateOf(0f) }
    val transition = updateTransition(targetState = selectedState, label = "transition")
    val animateOffset by transition.animateIntOffset(label = "animateOffset") {
        if (it) {
            IntOffset(xOffset.roundToInt(), yOffset.roundToInt())
        } else {
            IntOffset(0, 0)
        }
    }
    val dragAnimateOffset by transition.animateIntOffset(label = "dragAnimateOffset") {
        if (it) {
            IntOffset(xOffset.roundToInt(), yOffset.roundToInt())
        } else {
            IntOffset(dragX.roundToInt(), dragY.roundToInt())
        }
    }
    var isDragging by remember { mutableStateOf(false) }
    var isFromClick by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .size(92.dp, 46.dp)
            .onGloballyPositioned {
                startX = it.positionInWindow().x
                startY = it.positionInWindow().y
            }
            .graphicsLayer {
                if (isDragging) {
                    translationX = dragX
                    translationY = dragY
                } else if (!isFromClick) {
                    translationX = dragAnimateOffset.x.toFloat()
                    translationY = dragAnimateOffset.y.toFloat()
                } else {
                    translationX = animateOffset.x.toFloat()
                    translationY = animateOffset.y.toFloat()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        isDragging = false
                        Log.d("onDragEnd", "startY: $startY")
                        Log.d("onDragEnd", "dragY: $dragY")
                        Log.d("onDragEnd", "targetY + 100dp: ${targetY + with(density) { 100.dp.toPx() }}")
                        if (startY + dragY in 0f..targetY + with(density) { 100.dp.toPx() }) {
                            dragX = xOffset
                            dragY = yOffset
                            selectedState = true
                        } else {
                            dragX = 0f
                            dragY = 0f
                            selectedState = false
                        }
                    },
                    onDragStart = {
                        isFromClick = false
                        if (selectedState) {
                            dragX = xOffset
                            dragY = yOffset
                        } else {
                            dragX = 0f
                            dragY = 0f
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        isDragging = true
                        Log.d("pointerInput", "dragX: ${dragX + dragAmount.x}")
                        Log.d("pointerInput", "dragY: ${dragY + dragAmount.y}")
                        coroutineScope.launch {
                            dragX += dragAmount.x
                            dragY += dragAmount.y
                        }
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            coroutineScope.launch {
                isFromClick = true
                selectedState = !selectedState
            }
        }
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            text = text,
            textAlign = TextAlign.Center,
            color = Color.Black,
        )
    }
}

enum class SelectionState {
    Selected,
    UnSelected
}

@Preview(showBackground = true)
@Composable
fun FillInTheBlanksScreenV3Preview() {
    ComposePlaygroundTheme {
        FillInTheBlanksScreenV3()
    }
}