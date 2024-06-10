@file:OptIn(ExperimentalFoundationApi::class)

package com.example.composeplayground

import android.util.Log
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun FillInTheBlanksScreenV2() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val density = LocalDensity.current
            val coroutineScope = rememberCoroutineScope()
            var offset by remember { mutableFloatStateOf(0f) }
            val draggableState = remember {
                AnchoredDraggableState(
                    initialValue = DragAnchors.Start,
                    positionalThreshold = { distance: Float -> distance * .5f },
                    velocityThreshold = { with(density) { 100.dp.toPx() } },
                    snapAnimationSpec = tween(),
                    decayAnimationSpec = exponentialDecay(),
                )
            }
            var targetY by remember { mutableFloatStateOf(0f) }
            var startY by remember { mutableFloatStateOf(0f) }
            LaunchedEffect(startY, targetY) {
                val targetOffset = -(startY - targetY) + with(density) { 2.dp.toPx()}
                draggableState.updateAnchors(
                    DraggableAnchors {
                        Log.d("onGloballyPositioned", "Calculate Anchor Start ${targetOffset * DragAnchors.Start.fraction}")
                        Log.d("onGloballyPositioned", "Calculate Anchor End ${targetOffset * DragAnchors.End.fraction}")
                        DragAnchors.Start at targetOffset * DragAnchors.Start.fraction
                        DragAnchors.End at targetOffset * DragAnchors.End.fraction
                    }
                )
            }
            LaunchedEffect(draggableState.offset) {
                offset = draggableState.offset
            }
            Box(
                modifier = Modifier
                    .size(100.dp, 50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
                    .onGloballyPositioned {
                        Log.d("onGloballyPositioned", "BOX: $it")
                        targetY = it.positionInWindow().y
                    }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(
                    modifier = Modifier
                        .size(92.dp, 46.dp)
                        .onGloballyPositioned {
                            Log.d("onGloballyPositioned", "HELLO: $it")
                            startY = it.positionInWindow().y
                        }
                        .anchoredDraggable(
                            state = draggableState,
                            orientation = Orientation.Vertical,
                        )
                        .offset {
                            Log.d("onGloballyPositioned", "Offset Changed: $offset")
                            IntOffset(
                                x = 0,
                                y = offset.roundToInt()
                            )
                        },
                    onClick = {
                        if (draggableState.currentValue == DragAnchors.End) {
                            coroutineScope.launch {
                                draggableState.animateTo(DragAnchors.Start)
                            }
                        } else {
                            coroutineScope.launch {
                                draggableState.animateTo(DragAnchors.End)
                            }
                        }
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentHeight(),
                        text = "Hello",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

enum class DragAnchors(val fraction: Float) { Start(0f), End(1f) }

@Preview(showBackground = true)
@Composable
fun FillInTheBlanksScreenV2Preview() {
    ComposePlaygroundTheme {
        FillInTheBlanksScreenV2()
    }
}