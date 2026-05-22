package com.nammamela.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NammaMelaColors = lightColorScheme(
    primary = Color(0xFF006C67),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF9CF1EA),
    onPrimaryContainer = Color(0xFF00201E),
    secondary = Color(0xFFB63F2D),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDAD4),
    onSecondaryContainer = Color(0xFF3B0902),
    tertiary = Color(0xFF7A5B00),
    background = Color(0xFFF7FAF7),
    onBackground = Color(0xFF171D1C),
    surface = Color(0xFFF7FAF7),
    surfaceContainer = Color(0xFFEAF1EE),
    onSurface = Color(0xFF171D1C),
    onSurfaceVariant = Color(0xFF55615F),
    outlineVariant = Color(0xFFC4D0CD)
)

@Composable
fun NammaMelaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NammaMelaColors,
        content = content
    )
}
