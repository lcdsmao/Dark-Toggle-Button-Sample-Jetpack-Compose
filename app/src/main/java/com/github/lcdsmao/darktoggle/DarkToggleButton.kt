package com.github.lcdsmao.darktoggle

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.withSaveLayer
import androidx.ui.tooling.preview.Preview
import com.github.lcdsmao.darktoggle.ui.UiMode
import com.github.lcdsmao.darktoggle.ui.UiModeAmbient
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DarkToggleButton(
    modifier: Modifier = Modifier,
    springSpec: SpringSpec<Float> = remember { spring() },
) {
    var uiMode by UiModeAmbient.current
    val realSpringSpec = remember(springSpec) {
        spring(
            dampingRatio = springSpec.dampingRatio,
            stiffness = springSpec.stiffness,
            visibilityThreshold = 0.002f,
        )
    }
    OutlinedButton(
        modifier = modifier,
        onClick = { uiMode = uiMode.toggle() },
    ) {
        val sunMoonState = when (uiMode) {
            UiMode.Default -> SunMoonState.Sun
            UiMode.Dark -> SunMoonState.Moon
        }
        SunMoonIcon(sunMoonState, springSpec = realSpringSpec)
    }
}

private enum class SunMoonState {
    Sun,
    Moon,
    ;
}

private const val SurroundCircleNum = 8
private val rotation = FloatPropKey()
private val maskCxRatio = FloatPropKey()
private val maskCyRatio = FloatPropKey()
private val maskRadiusRatio = FloatPropKey()
private val circleRadiusRatio = FloatPropKey()
private val surroundCircleScales = List(SurroundCircleNum) { FloatPropKey() }
private val surroundCircleAlphas = List(SurroundCircleNum) { FloatPropKey() }

private fun sunMoonTransition(
    springSpec: SpringSpec<Float>,
) = transitionDefinition<SunMoonState> {
    state(SunMoonState.Sun) {
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

    state(SunMoonState.Moon) {
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

    transition(
        SunMoonState.Moon to SunMoonState.Sun,
    ) {
        rotation using springSpec
        maskCxRatio using springSpec
        maskCyRatio using springSpec
        maskRadiusRatio using springSpec
        circleRadiusRatio using springSpec

        repeat(SurroundCircleNum) { i ->
            val delayUnit = (-springSpec.stiffness * 0.067f + 55).toInt().coerceIn(5, 50)
            val tween = tween<Float>(delayMillis = i * delayUnit)
            surroundCircleAlphas[i] using tween
            surroundCircleScales[i] using tween
        }
    }

    transition(
        SunMoonState.Sun to SunMoonState.Moon,
    ) {
        rotation using springSpec
        maskCxRatio using springSpec
        maskCyRatio using springSpec
        maskRadiusRatio using springSpec
        circleRadiusRatio using springSpec
    }
}

@Composable
private fun SunMoonIcon(
    sunMoonState: SunMoonState,
    modifier: Modifier = Modifier,
    springSpec: SpringSpec<Float>,
    fillColor: Color = MaterialTheme.colors.onSurface,
) {
    val state = transition(
        definition = remember(springSpec) { sunMoonTransition(springSpec) },
        toState = sunMoonState,
    )
    Canvas(
        modifier = modifier.aspectRatio(1f)
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
                val radians = PI / 2 - i * 2 * PI / SurroundCircleNum
                val d = sizePx / 3
                val cx = center.x + d * cos(radians)
                val cy = center.y - d * sin(radians)
                drawCircle(
                    color = fillColor,
                    radius = sizePx * 0.05f,
                    center = Offset(cx.toFloat(), cy.toFloat()),
                    alpha = state[surroundCircleAlphas[i]].coerceIn(0f, 1f),
                )
            }
        }
    }
}

@Preview(widthDp = 64, heightDp = 64)
@Composable
fun PreviewSunIcon() {
    SunMoonIcon(sunMoonState = SunMoonState.Sun, springSpec = spring())
}

@Preview(widthDp = 64, heightDp = 64)
@Composable
fun PreviewMoonIcon() {
    SunMoonIcon(sunMoonState = SunMoonState.Moon, springSpec = spring())
}
