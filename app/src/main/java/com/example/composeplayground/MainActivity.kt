package com.example.composeplayground

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.NativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.composeplayground.ui.theme.ComposePlaygroundTheme

// References:
// https://github.com/riggaroo/compose-playtime/blob/main/app/src/main/java/dev/riggaroo/composeplaytime/SmoothLineGraph.kt
// https://www.youtube.com/watch?v=1yiuxWK74vI
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    ComposePlaygroundTheme {
        NavHost(
            modifier = Modifier.padding(),
            navController = navController,
            startDestination = NavigationRoute.Main,
            enterTransition = {
                slideInVertically(initialOffsetY = { it })
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -it })
            },
            popEnterTransition = {
                slideInVertically(initialOffsetY = { -it })
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { it })
            }
        ) {
            composable<NavigationRoute.Main> { MainScreen { navController.navigate(it) } }
            composable<NavigationRoute.Basics> { BasicsScreen() }
            composable<NavigationRoute.Graph> { GraphScreen() }
            composable<NavigationRoute.Drawing> {
                var bitmap by remember { mutableStateOf<Bitmap?>(null) }
                DrawingScreen(bitmap) { picture ->
                    bitmap = Bitmap.createBitmap(picture.width, picture.height, Bitmap.Config.ARGB_8888).run {
                        val canvas = NativeCanvas(this)
                        canvas.drawColor(android.graphics.Color.WHITE)
                        canvas.drawPicture(picture)
                        Log.d("ComposeDrawingActivity", "DrawingCanvas: $this")
                        this
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonsPreview() {
    ComposePlaygroundTheme {
        App()
    }
}
