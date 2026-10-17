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
    background = HireKoroBackground,
    onBackground = HireKoroCharcoal,
    surface = HireKoroWhite,
    onSurface = HireKoroBlack,
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = HireKoroCharcoal,
    outline = Color(0xFFCCCCCC)
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
