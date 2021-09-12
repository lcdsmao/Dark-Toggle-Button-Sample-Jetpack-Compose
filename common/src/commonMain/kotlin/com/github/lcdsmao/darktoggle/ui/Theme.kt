package com.github.lcdsmao.darktoggle.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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

val LocalUiMode = staticCompositionLocalOf<MutableState<UiMode>> {
    error("UiMode not provided")
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val isSystemDark = isSystemInDarkTheme()
    val uiMode = remember {
        mutableStateOf(if (isSystemDark) UiMode.Dark else UiMode.Default)
    }
    CompositionLocalProvider(LocalUiMode provides uiMode) {
        val colors = when (LocalUiMode.current.value) {
            UiMode.Default -> LightColorPalette
            UiMode.Dark -> DarkColorPalette
        }

        MaterialTheme(
            colors = animate(colors),
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

@Composable
private fun animate(colors: Colors): Colors {
    val animSpec = remember {
        spring<Color>(stiffness = 500f)
    }

    @Composable
    fun animateColor(color: Color): Color = animateColorAsState(targetValue = color, animationSpec = animSpec).value

    return Colors(
        primary = animateColor(colors.primary),
        primaryVariant = animateColor(colors.primaryVariant),
        secondary = animateColor(colors.secondary),
        secondaryVariant = animateColor(colors.secondaryVariant),
        background = animateColor(colors.background),
        surface = animateColor(colors.surface),
        error = animateColor(colors.error),
        onPrimary = animateColor(colors.onPrimary),
        onSecondary = animateColor(colors.onSecondary),
        onBackground = animateColor(colors.onBackground),
        onSurface = animateColor(colors.onSurface),
        onError = animateColor(colors.onError),
        isLight = colors.isLight,
    )
}
