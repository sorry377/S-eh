package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NovaDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = TextDark,
    secondary = ElectricPurple,
    onSecondary = TextWhite,
    tertiary = LaserPink,
    onTertiary = TextWhite,
    background = DarkObsidianBg,
    onBackground = TextWhite,
    surface = DarkSurfaceGlass,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceCard,
    onSurfaceVariant = TextMuted,
    outline = DarkBorderGlow
)

@Composable
fun NovaAITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NovaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
