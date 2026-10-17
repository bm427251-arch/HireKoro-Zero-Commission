package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HireKoroDarkColorScheme = darkColorScheme(
    primary = HireKoroYellow,
    onPrimary = HireKoroBlack,
    primaryContainer = HireKoroCharcoal,
    onPrimaryContainer = HireKoroYellow,
    secondary = HireKoroYellow,
    onSecondary = HireKoroBlack,
    background = HireKoroCharcoal,
    onBackground = HireKoroWhite,
    surface = Color(0xFF1E1E1E),
    onSurface = HireKoroWhite,
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = Color(0xFFE0E0E0),
    outline = Color(0xFF555555)
)

private val HireKoroLightColorScheme = lightColorScheme(
    primary = HireKoroYellow,
    onPrimary = HireKoroBlack,
    primaryContainer = Color(0xFFFFF7C2),
    onPrimaryContainer = HireKoroBlack,
    secondary = HireKoroCharcoal,
    onSecondary = HireKoroWhite,
    secondaryContainer = Color(0xFFEAEAEA),
    onSecondaryContainer = HireKoroBlack,
    background = HireKoroBackground,
    onBackground = HireKoroCharcoal,
    surface = HireKoroWhite,
    onSurface = HireKoroBlack,
    surfaceVariant = HireKoroSurfaceVariant,
    onSurfaceVariant = HireKoroCharcoal,
    outline = HireKoroOutline
)

@Composable
fun HireKoroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) HireKoroDarkColorScheme else HireKoroLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
