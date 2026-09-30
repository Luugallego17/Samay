package com.samay.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Solo tema claro a propósito: fondos cálidos y alto contraste en momentos de crisis.
private val LightColors = lightColorScheme(
    primary = SamayForest,
    onPrimary = SamayOnDark,
    primaryContainer = SamaySage,
    onPrimaryContainer = SamayForest,
    secondary = SamayForestSoft,
    onSecondary = SamayOnDark,
    background = SamayCream,
    onBackground = SamayForest,
    surface = SamayCream,
    onSurface = SamayForest,
    surfaceVariant = SamaySurface,
    onSurfaceVariant = SamayMuted,
    outline = SamayOutline,
    error = SamayCrisis,
    onError = SamayOnDark,
    errorContainer = SamayCrisisSoft
)

@Composable
fun SamayTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = SamayTypography,
        content = content
    )
}
