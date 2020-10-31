package com.github.lcdsmao.darktoggle

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

                DarkToggleButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally).weight(1f)
                )
            }
        }
    }
}
