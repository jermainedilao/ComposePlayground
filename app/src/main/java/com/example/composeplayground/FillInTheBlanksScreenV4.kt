@file:OptIn(ExperimentalFoundationApi::class)

package com.example.composeplayground

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlinx.coroutines.launch

@Composable
fun FillInTheBlanksScreenV4() {
    Scaffold { paddingValues ->
        var targetX by remember { mutableFloatStateOf(Float.NaN) }
        var targetY by remember { mutableFloatStateOf(Float.NaN) }
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
    var startX by remember { mutableFloatStateOf(Float.NaN) }
    var startY by remember { mutableFloatStateOf(Float.NaN) }
    val xOffset by remember(targetX, startX) {
        mutableFloatStateOf(
            if (!startY.isNaN() && !targetY.isNaN()) {
                if (targetX < startX) {
                    -(startX - targetX)
                } else {
                    targetX - startX
                } + with(density) { 4.dp.toPx() }
            } else {
                Float.NaN
            }
        )
    }
    val yOffset by remember(targetY, startY) {
        mutableFloatStateOf(
            if (!startY.isNaN() && !targetY.isNaN()) {
                -(startY - targetY - with(density) { 2.dp.toPx() })
            } else {
                Float.NaN
            }
        )
    }
    val animatableY = remember(yOffset) {
        Animatable(0f).apply {
            if (!yOffset.isNaN()) {
                updateBounds(yOffset, 0f)
            }
        }
    }
    val decay = rememberSplineBasedDecay<Float>()
    Card(
        modifier = Modifier
            .size(92.dp, 46.dp)
            .onGloballyPositioned {
                startX = it.positionInWindow().x
                startY = it.positionInWindow().y
            }
            .graphicsLayer {
                Log.d("graphicsLayer", "${animatableY.value / yOffset}")
                if (!xOffset.isNaN() && !yOffset.isNaN()) {
                    translationX = xOffset * (animatableY.value / yOffset).coerceIn(0f, 1f)
                    translationY = animatableY.value.coerceIn(yOffset, 0f)
                }
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    selectedState = !selectedState
                    coroutineScope.launch {
                        if (selectedState) {
                            animatableY.animateTo(yOffset)
                        } else {
                            animatableY.animateTo(0f)
                        }
                    }
                },
            )
            .draggable(
                state = rememberDraggableState {
                    Log.d("rememberDraggableState", "dragAmount: $it")
                    coroutineScope.launch {
                        animatableY.snapTo((animatableY.value + it))
                    }
                    Log.d("rememberDraggableState", "animatableY: ${animatableY.value}")
                },
                orientation = Orientation.Vertical,
                onDragStopped = { velocity ->
                    val decayY = decay.calculateTargetValue(
                        animatableY.value,
                        velocity
                    )
                    val isInsideSelectedBounds = decayY < yOffset + with(density) { 50.dp.toPx() }
//                    val isInsideSelectedBounds = animatableY.value < yOffset + with(density) { 50.dp.toPx() }
                    val postDragTargetY = if (isInsideSelectedBounds) {
                        yOffset
                    } else {
                        0f
                    }
                    val canReachTargetWithDecay = (decayY < yOffset && postDragTargetY == yOffset) ||
                        (decayY > yOffset && postDragTargetY == 0f)
//                    val canReachTargetWithDecay = (animatableY.value < yOffset && postDragTargetY == yOffset) ||
//                        (animatableY.value > yOffset && postDragTargetY == 0f)
                    Log.d("rememberDraggableState", "onDragStopped.targetY: $targetY")
                    Log.d("rememberDraggableState", "onDragStopped.yOffset: $yOffset")
                    Log.d("rememberDraggableState", "onDragStopped.velocity: $velocity")
                    Log.d("rememberDraggableState", "onDragStopped.isInsideSelectedBounds: $isInsideSelectedBounds")
                    Log.d("rememberDraggableState", "onDragStopped.decayY: $decayY")
                    Log.d(
                        "rememberDraggableState",
                        "onDragStopped.selectBounds: ${yOffset - with(density) { 100.dp.toPx() }}"
                    )
                    Log.d("rememberDraggableState", "onDragStopped.postDragTargetY: $postDragTargetY")
                    Log.d("rememberDraggableState", "onDragStopped.canReachTargetWithDecay: $canReachTargetWithDecay")
                    launch {
                        if (canReachTargetWithDecay) {
                            Log.d("rememberDraggableState", "onDragStopped.animatableY: ${animatableY.value}")
                            Log.d("rememberDraggableState", "onDragStopped.initialVelocity: $velocity")
                            val result = animatableY.animateDecay(
                                initialVelocity = /*if (postDragTargetY == yOffset) {*/

                                velocity
                                /*} else {
                                    Log.d("rememberDraggableState", "onDragStopped.initialVelocity: ${-velocity}")
                                    -velocity
                                }*/,
                                animationSpec = decay
                            )
                            when (result.endReason) {
                                AnimationEndReason.BoundReached -> {
                                    Log.d("rememberDraggableState", "onDragStopped.boundReached")
                                    // do nothing
                                }
                                AnimationEndReason.Finished -> {
                                    Log.d("rememberDraggableState", "onDragStopped.Finished")
                                    animatableY.animateTo(postDragTargetY, initialVelocity = velocity)
                                }
                            }
                        } else {
                            animatableY.animateTo(postDragTargetY, initialVelocity = velocity)
                        }
                        selectedState = postDragTargetY == yOffset
                    }
                },
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(4.dp),
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

@Preview(showBackground = true)
@Composable
fun FillInTheBlanksScreenV4Preview() {
    ComposePlaygroundTheme {
        FillInTheBlanksScreenV4()
    }
}