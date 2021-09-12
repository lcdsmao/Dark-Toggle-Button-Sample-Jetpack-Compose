package com.github.lcdsmao.darktoggle

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@ExperimentalAnimationApi
fun main() = application {
    Window(title = "DarkToggleButton", onCloseRequest = ::exitApplication) {
        App()
    }
}
