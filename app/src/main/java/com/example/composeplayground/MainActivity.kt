package com.example.composeplayground

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme

// References:
// https://github.com/riggaroo/compose-playtime/blob/main/app/src/main/java/dev/riggaroo/composeplaytime/SmoothLineGraph.kt
// https://www.youtube.com/watch?v=1yiuxWK74vI
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Buttons(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Buttons(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current
        Button(
            onClick = {
                context.startActivity(Intent(context, BasicsActivity::class.java))
            }
        ) {
            Text(text = "Open Basics")
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, GraphActivity::class.java))
            }
        ) {
            Text(text = "Open Graph")
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, ComposeDrawingActivity::class.java))
            }
        ) {
            Text(text = "Open Drawing")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonsPreview() {
    ComposePlaygroundTheme {
        Buttons()
    }
}
