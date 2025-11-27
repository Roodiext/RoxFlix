package com.viona.roxflix.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.viona.roxflix.R

@Composable
fun VoiceSearchButton(onStartVoice: () -> Unit) {

    // ✅ Pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(950, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAnim"
    )

    // ✅ Gradient adapts automatically to theme
    val isLight = !isSystemInDarkTheme()
    val gradientColors = if (isLight) {
        listOf(Color(0xFFE50914), Color(0xFFB20710))
    } else {
        listOf(Color(0xFFB20710), Color(0xFF7A050A))
    }

    Box(
        modifier = Modifier
            .size(58.dp)
            .scale(scale)
            .shadow(10.dp, CircleShape, clip = false)
            .background(
                brush = Brush.linearGradient(gradientColors),
                shape = CircleShape
            )
            .clickable { onStartVoice() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Mic,
            contentDescription = stringResource(R.string.voice_search),
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}
