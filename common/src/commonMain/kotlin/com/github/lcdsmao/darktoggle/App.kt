package com.github.lcdsmao.darktoggle

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.format
// import androidx.ui.tooling.preview.Devices
// import androidx.ui.tooling.preview.Preview
import com.github.lcdsmao.darktoggle.ui.AppTheme
import com.github.lcdsmao.darktoggle.ui.UiModeAmbient

@Composable
fun App() {
    AppTheme {
        Surface(color = MaterialTheme.colors.surface) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.size(32.dp))
                Text(
                    text = "UI Mode",
                    style = MaterialTheme.typography.h3,
                )
                Spacer(modifier = Modifier.size(16.dp))

                val uiMode by UiModeAmbient.current
                Crossfade(current = uiMode) {
                    Text(
                        text = uiMode.name,
                        style = MaterialTheme.typography.h5,
                    )
                }

                var dampingRatio by remember { mutableStateOf(0.5f) }
                var stiffness by remember { mutableStateOf(100f) }
                val springSpec = remember(dampingRatio, stiffness) {
                    spring<Float>(dampingRatio = dampingRatio, stiffness = stiffness)
                }
                DarkToggleButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .weight(1f)
                        .size(120.dp),
                    springSpec = springSpec,
                )

                Text("Damping Ratio: %.2f".format(dampingRatio))
                Slider(
                    value = dampingRatio,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onValueChange = { dampingRatio = it },
                    valueRange = 0.2f..1f
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text("Stiffness: %.0f".format(stiffness))
                Slider(
                    value = stiffness,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onValueChange = { stiffness = it },
                    valueRange = 50f..800f,
                )

                Spacer(modifier = Modifier.size(32.dp))
            }
        }
    }
}

// @Preview(device = Devices.PIXEL_4)
// @Composable
// fun PreviewApp() {
//     App()
// }
