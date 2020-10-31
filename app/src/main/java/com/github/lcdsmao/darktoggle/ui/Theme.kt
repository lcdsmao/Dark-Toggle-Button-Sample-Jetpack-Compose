package com.github.lcdsmao.darktoggle.ui

import android.content.res.Configuration
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.platform.ConfigurationAmbient

private val DarkColorPalette = darkColors(
    primary = purple200,
    primaryVariant = purple700,
    secondary = teal200
)

private val LightColorPalette = lightColors(
    primary = purple500,
    primaryVariant = purple700,
    secondary = teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

enum class UiMode {
    Default,
    Dark,
    ;

    fun toggle(): UiMode = when (this) {
        Default -> Dark
        Dark -> Default
    }
}

val UiModeAmbient = staticAmbientOf<MutableState<UiMode>>()

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val currentSystemUiMode =
        ConfigurationAmbient.current.uiMode and Configuration.UI_MODE_NIGHT_MASK
    val uiMode = remember {
        mutableStateOf(
            if (currentSystemUiMode == Configuration.UI_MODE_NIGHT_YES) UiMode.Dark
            else UiMode.Default
        )
    }
    Providers(UiModeAmbient provides uiMode) {
        val colors = when (UiModeAmbient.current.value) {
            UiMode.Default -> LightColorPalette
            UiMode.Dark -> DarkColorPalette
        }

        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}
