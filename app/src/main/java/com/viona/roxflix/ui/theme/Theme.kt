package com.viona.roxflix.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    background = BlackBackground,
    surface = CardDark,
    primary = RoxRed,
    onPrimary = Color.White,
    onBackground = OnDark,
    onSurface = OnDark
)

private val LightColors = lightColorScheme(
    background = WhiteBackground,
    surface = CardLight,
    primary = RoxRed,
    onPrimary = Color.White,
    onBackground = OnLight,
    onSurface = OnLight
)

@Composable
fun RoxFlixTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
