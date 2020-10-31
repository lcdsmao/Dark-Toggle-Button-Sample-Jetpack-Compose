package com.github.lcdsmao.darktoggle

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.lcdsmao.darktoggle.ui.UiMode
import com.github.lcdsmao.darktoggle.ui.UiModeAmbient
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DarkToggleButton(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
) {
    var uiMode by UiModeAmbient.current
    OutlinedButton(
        modifier = modifier.size(size),
        onClick = { uiMode = uiMode.toggle() },
    ) {
        SunMoonIcon(uiMode)
    }
}

private const val SurroundCircleNum = 8
private val rotation = FloatPropKey()
private val maskCxRatio = FloatPropKey()
private val maskCyRatio = FloatPropKey()
private val maskRadiusRatio = FloatPropKey()
private val circleRadiusRatio = FloatPropKey()
private val surroundCircleScales = List(SurroundCircleNum) { FloatPropKey() }
private val surroundCircleAlphas = List(SurroundCircleNum) { FloatPropKey() }

private val transitionDefinition = transitionDefinition<UiMode> {
    state(UiMode.Default) {
        this[rotation] = 180f
        this[maskCxRatio] = 1f
        this[maskCyRatio] = 0f
        this[maskRadiusRatio] = 0.125f
        this[circleRadiusRatio] = 0.2f
        repeat(SurroundCircleNum) {
            this[surroundCircleScales[it]] = 1f
            this[surroundCircleAlphas[it]] = 1f
        }
    }

    state(UiMode.Dark) {
        this[rotation] = 45f
        this[maskCxRatio] = 0.5f
        this[maskCyRatio] = 0.18f
        this[maskRadiusRatio] = 0.35f
        this[circleRadiusRatio] = 0.35f
        repeat(SurroundCircleNum) {
            this[surroundCircleScales[it]] = 0f
            this[surroundCircleAlphas[it]] = 0f
        }
    }

    val defaultSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow,
    )

    transition(
        UiMode.Dark to UiMode.Default,
    ) {
        rotation using defaultSpring
        maskCxRatio using defaultSpring
        maskCyRatio using defaultSpring
        maskRadiusRatio using defaultSpring
        circleRadiusRatio using defaultSpring

        repeat(SurroundCircleNum) {
            val tween = tween<Float>(delayMillis = it * 50)
            surroundCircleAlphas[it] using tween
            surroundCircleScales[it] using tween
        }
    }

    transition(
        UiMode.Default to UiMode.Dark,
    ) {
        rotation using defaultSpring
        maskCxRatio using defaultSpring
        maskCyRatio using defaultSpring
        maskRadiusRatio using defaultSpring
        circleRadiusRatio using defaultSpring
    }
}

@Composable
private fun SunMoonIcon(
    uiMode: UiMode,
    modifier: Modifier = Modifier,
    fillColor: Color = MaterialTheme.colors.onSurface,
    surfaceColor: Color = MaterialTheme.colors.surface,
) {
    val state = transition(definition = transitionDefinition, toState = uiMode)
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val sizePx = size.width

        drawContext.transform.rotate(state[rotation])
        drawContext.canvas.withSaveLayer(
            bounds = drawContext.size.toRect(),
            paint = Paint()
        ) {

            drawCircle(
                color = fillColor,
                radius = sizePx * state[circleRadiusRatio],
            )

            drawCircle(
                color = Color.Black,
                radius = sizePx * state[maskRadiusRatio],
                center = Offset(
                    x = size.width * state[maskCxRatio],
                    y = size.height * state[maskCyRatio],
                ),
                blendMode = BlendMode.DstOut,
            )
        }

        repeat(SurroundCircleNum) { i ->
            scale(scale = state[surroundCircleScales[i]]) {
                val radians = PI / 2 - (i * PI) / 4
                val d = sizePx / 3
                val cx = center.x + d * cos(radians)
                val cy = center.y - d * sin(radians)
                drawCircle(
                    color = fillColor,
                    radius = sizePx * 0.055f,
                    center = Offset(cx.toFloat(), cy.toFloat()),
                    alpha = state[surroundCircleAlphas[i]],
                )
            }
        }
    }
}
