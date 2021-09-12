package com.github.lcdsmao.darktoggle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
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
import com.github.lcdsmao.darktoggle.ui.AppTheme
import com.github.lcdsmao.darktoggle.ui.LocalUiMode
import kotlin.math.roundToInt

@ExperimentalAnimationApi
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

                val uiMode by LocalUiMode.current
                AnimatedContent(targetState = uiMode) { targetUiMode ->
                    Text(
                        text = targetUiMode.name,
                        style = MaterialTheme.typography.h5,
                    )
                }

                var dampingRatio by remember { mutableStateOf(0.5f) }
                var stiffness by remember { mutableStateOf(100f) }
                val springSpec = remember(dampingRatio, stiffness) {
                    spring<Float>(dampingRatio = dampingRatio, stiffness = stiffness)
                }

                Box(modifier = Modifier.weight(1f)) {
                    DarkToggleButton(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(120.dp),
                        springSpec = springSpec,
                    )
                }

                Text("Damping Ratio: ${dampingRatio.roundToDecimals(2)}")
                Slider(
                    value = dampingRatio,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onValueChange = { dampingRatio = it },
                    valueRange = 0.2f..1f
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text("Stiffness: ${stiffness.roundToInt()}")
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

private fun Float.roundToDecimals(decimals: Int): Float {
    var dotAt = 1
    repeat(decimals) { dotAt *= 10 }
    val roundedValue = (this * dotAt).roundToInt()
    return (roundedValue / dotAt) + (roundedValue % dotAt).toFloat() / dotAt
}
