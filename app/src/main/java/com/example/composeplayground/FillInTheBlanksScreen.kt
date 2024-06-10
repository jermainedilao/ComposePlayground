@file:OptIn(ExperimentalFoundationApi::class)

package com.example.composeplayground

import android.content.ClipData
import android.util.Log
import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlin.math.roundToInt

@Composable
fun FillInTheBlanksScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var targetPosition by remember { mutableStateOf(Offset.Unspecified) }
            Row {
                Text(
                    text = "The quick brown ",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .onGloballyPositioned {
//                            val targetX = it.size.width
                            targetPosition = it.positionInWindow()
                            Log.d(
                                "FillInTheBlanksScreen",
                                "textPositionX: ${it.positionInWindow().x}, textPositionY: ${it.positionInWindow().y}"
                            )
                        },
                    text = "_ _ _ _",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = " jumps over the lazy dog",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Dog",
                    modifier = Modifier
                        .dragAndDropSource {
                            detectTapGestures(onPress = {
                                startTransfer(
                                    DragAndDropTransferData(
                                        ClipData.newPlainText("dog", "dog"),
                                        flags = View.DRAG_FLAG_GLOBAL
                                    )
                                )
                            })
                        }
                        .graphicsLayer { alpha = 1f }
                        .background(Color.White)
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(100.dp))
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black,
                )
                val padding = with(LocalDensity.current) {
                    -8.dp.toPx()
                }
                var offsetFoxX by remember { mutableStateOf(0f) }
                var offsetFoxY by remember { mutableStateOf(0f) }
                var positionFoxX by remember { mutableStateOf(0f) }
                var positionFoxY by remember { mutableStateOf(0f) }
                var buttonHeight by remember { mutableStateOf(0) }
                OutlinedButton(
                    modifier = Modifier
                        .onGloballyPositioned {
                            positionFoxX = it.positionInWindow().x - padding
                            positionFoxY = it.positionInWindow().y
                            buttonHeight = it.size.height

                            Log.d(
                                "FillInTheBlanksScreen",
                                "buttonPositionX: ${it.positionInWindow().x}, buttonPositionY: ${it.positionInWindow().y}"
                            )
                        }
                        .offset {
                            Log.d(
                                "FillInTheBlanksScreen",
                                "positionMinusTargetX: ${positionFoxX - targetPosition.x}, positionMinusTargetX: ${positionFoxY - targetPosition.y}"
                            )
                            IntOffset(offsetFoxX.roundToInt(), offsetFoxY.roundToInt())
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetFoxX += dragAmount.x
                                    offsetFoxY += dragAmount.y

                                    Log.d(
                                        "FillInTheBlanksScreen",
                                        "changeX: ${change.position.x}, changeY: ${change.position.y}"
                                    )
                                    Log.d("FillInTheBlanksScreen", "offsetFoxX: $offsetFoxX, offsetFoxY: $offsetFoxY")
                                },
                                onDragEnd = {
                                    val isInside =
                                        positionFoxY + offsetFoxY in targetPosition.y..targetPosition.y + buttonHeight + 1.5f
                                    if (targetPosition != Offset.Unspecified && isInside) {
                                        offsetFoxX = positionFoxX - targetPosition.x
                                        offsetFoxY = -(positionFoxY - targetPosition.y)
                                    } else {
                                        offsetFoxX = 0f
                                        offsetFoxY = 0f
                                    }
                                }
                            )
                        },
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text(text = "Fox")
                }
                var offsetCatX by remember { mutableStateOf(0f) }
                var offsetCatY by remember { mutableStateOf(0f) }
                OutlinedButton(
                    modifier = Modifier
                        .offset { (IntOffset(offsetCatX.roundToInt(), offsetCatY.roundToInt())) }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {

                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetCatX += dragAmount.x
                                    offsetCatY += dragAmount.y
                                }
                            )
                        },
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text(text = "Cat")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FillInTheBlanksScreenPreview() {
    ComposePlaygroundTheme {
        FillInTheBlanksScreen()
    }
}