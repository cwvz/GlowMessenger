package com.example.glowmessager.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Draws a blurred outer neon glow behind the layout element.
 */
fun Modifier.neonGlow(
    color: Color = NeonCyan,
    radius: Dp = 12.dp,
    alpha: Float = 0.5f,
    cornerRadius: Dp = 16.dp
): Modifier = this.drawBehind {
    if (radius <= 0.dp || alpha <= 0f) return@drawBehind

    val shadowColor = color.copy(alpha = alpha).toArgb()
    drawIntoCanvas { canvas ->
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            this.color = shadowColor
            maskFilter = BlurMaskFilter(radius.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        canvas.nativeCanvas.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            cornerRadius.toPx(),
            cornerRadius.toPx(),
            paint
        )
    }
}

/**
 * Draws a circular neon glow behind round elements like avatars or floating buttons.
 */
fun Modifier.neonCircularGlow(
    color: Color = NeonCyan,
    radius: Dp = 12.dp,
    alpha: Float = 0.5f
): Modifier = this.drawBehind {
    if (radius <= 0.dp || alpha <= 0f) return@drawBehind

    val shadowColor = color.copy(alpha = alpha).toArgb()
    drawIntoCanvas { canvas ->
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            this.color = shadowColor
            maskFilter = BlurMaskFilter(radius.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        val center = size.width / 2f
        canvas.nativeCanvas.drawCircle(
            center,
            center,
            center,
            paint
        )
    }
}

/**
 * Combined neon border and outer blurred glow modifier.
 */
fun Modifier.neonBorder(
    color: Color = NeonCyan,
    borderWidth: Dp = 1.5.dp,
    cornerRadius: Dp = 16.dp,
    glowRadius: Dp = 8.dp,
    glowAlpha: Float = 0.4f
): Modifier = this
    .neonGlow(
        color = color,
        radius = glowRadius,
        alpha = glowAlpha,
        cornerRadius = cornerRadius
    )
    .border(
        width = borderWidth,
        color = color,
        shape = RoundedCornerShape(cornerRadius)
    )

/**
 * Pulsing animated neon glow modifier.
 */
@Composable
fun Modifier.pulsingNeonGlow(
    color: Color = NeonCyan,
    minAlpha: Float = 0.2f,
    maxAlpha: Float = 0.8f,
    durationMillis: Int = 1200,
    radius: Dp = 12.dp,
    cornerRadius: Dp = 16.dp
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsingGlowTransition")
    val alpha by infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsingGlowAlpha"
    )

    return this.neonGlow(
        color = color,
        radius = radius,
        alpha = alpha,
        cornerRadius = cornerRadius
    )
}

/**
 * Glowing Neon Card component for dark theme UI.
 */
@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    glowColor: Color = NeonCyan,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .neonBorder(
                color = glowColor,
                cornerRadius = cornerRadius,
                borderWidth = 1.dp,
                glowRadius = 8.dp,
                glowAlpha = 0.35f
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(DarkCardBackground)
    ) {
        content()
    }
}

/**
 * Neon online status badge with optional pulsing animation.
 */
@Composable
fun NeonBadge(
    modifier: Modifier = Modifier,
    color: Color = OnlineGreen,
    size: Dp = 12.dp,
    isPulsing: Boolean = true
) {
    val baseModifier = modifier
        .size(size)
        .clip(CircleShape)

    val finalModifier = if (isPulsing) {
        baseModifier.pulsingNeonGlow(
            color = color,
            minAlpha = 0.3f,
            maxAlpha = 0.9f,
            radius = 6.dp,
            cornerRadius = size / 2
        )
    } else {
        baseModifier.neonCircularGlow(
            color = color,
            radius = 4.dp,
            alpha = 0.5f
        )
    }

    Box(
        modifier = finalModifier.background(color, CircleShape)
    )
}
