package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.data.model.ThemeMode

private val LightColorScheme = lightColorScheme(
    primary = MedBluePrimary,
    onPrimary = MedBlueOnPrimary,
    primaryContainer = MedBluePrimaryContainer,
    onPrimaryContainer = MedBlueOnPrimaryContainer,
    secondary = MedTealSecondary,
    onSecondary = MedTealOnSecondary,
    secondaryContainer = MedTealSecondaryContainer,
    onSecondaryContainer = MedTealOnSecondaryContainer,
    tertiary = MedIndigoTertiary,
    tertiaryContainer = MedIndigoContainer,
    background = MedLightBackground,
    onBackground = MedLightOnSurface,
    surface = MedLightSurface,
    onSurface = MedLightOnSurface,
    surfaceVariant = MedLightSurfaceVariant,
    onSurfaceVariant = MedLightOnSurfaceVariant,
    outline = MedLightOutline,
    error = AlertSeriousRed,
    errorContainer = AlertSeriousContainer
)

private val ColorErrorDark = androidx.compose.ui.graphics.Color(0xFFFCA5A5)

private val DarkColorScheme = darkColorScheme(
    primary = MedDarkPrimary,
    onPrimary = MedDarkOnPrimary,
    primaryContainer = MedDarkPrimaryContainer,
    onPrimaryContainer = MedDarkOnPrimaryContainer,
    secondary = MedDarkSecondary,
    onSecondary = MedDarkOnSecondary,
    secondaryContainer = MedDarkSecondaryContainer,
    onSecondaryContainer = MedDarkOnSecondaryContainer,
    tertiary = MedDarkPrimary,
    tertiaryContainer = MedDarkSurfaceVariant,
    background = MedDarkBackground,
    onBackground = MedDarkOnSurface,
    surface = MedDarkSurface,
    onSurface = MedDarkOnSurface,
    surfaceVariant = MedDarkSurfaceVariant,
    onSurfaceVariant = MedDarkOnSurfaceVariant,
    outline = MedDarkOutline,
    error = ColorErrorDark,
    errorContainer = AlertSeriousDarkContainer
)

val MediGuideShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun MediGuideTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    MaterialTheme(
        colorScheme = if (useDark) DarkColorScheme else LightColorScheme,
        typography = MediGuideTypography,
        shapes = MediGuideShapes,
        content = content
    )
}
