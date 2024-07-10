package com.example.composeplayground

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

@Composable
fun MainScreen(
    onNavigateClick: (route: NavigationRoute) -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val context = LocalContext.current
            Button(onClick = { onNavigateClick.invoke(NavigationRoute.Basics) }) {
                Text(text = "Open Basics")
            }
            Button(onClick = { onNavigateClick.invoke(NavigationRoute.Graph) }) {
                Text(text = "Open Graph")
            }
            Button(onClick = { onNavigateClick.invoke(NavigationRoute.Drawing) }) {
                Text(text = "Open Drawing")
            }
            Button(onClick = { onNavigateClick.invoke(NavigationRoute.FillInTheBlanks) }) {
                Text(text = "Open Fill In The Blanks")
            }
            Button(onClick = { onNavigateClick.invoke(NavigationRoute.FillInTheBlanksV2) }) {
                Text(text = "Open Fill In The Blanks V2")
            }
            Button(onClick = { onNavigateClick.invoke(NavigationRoute.FillInTheBlanksV3) }) {
                Text(text = "Open Fill In The Blanks V3")
            }
            Button(onClick = { onNavigateClick.invoke(NavigationRoute.FillInTheBlanksV4) }) {
                Text(text = "Open Fill In The Blanks V4")
            }
            Button(onClick = { onNavigateClick.invoke(NavigationRoute.Coil) }) {
                Text(text = "Open Coil")
            }
        }
    }
}