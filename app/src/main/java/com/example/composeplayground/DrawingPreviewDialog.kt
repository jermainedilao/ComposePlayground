package com.example.composeplayground

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun DrawingDialog(bitmap: Bitmap) {
    Box {
        Card(
            modifier = Modifier.align(Alignment.Center),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Image(
                modifier = Modifier.wrapContentSize(),
                bitmap = bitmap.asImageBitmap(), contentDescription = "drawing"
            )
        }
    }
}