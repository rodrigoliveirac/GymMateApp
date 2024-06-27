package com.rodcollab.gymmateapp.core.ui.theme
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.rodcollab.gymmateapp.ui.theme.backgroundDark
import com.rodcollab.gymmateapp.ui.theme.backgroundDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.backgroundDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.backgroundLight
import com.rodcollab.gymmateapp.ui.theme.backgroundLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.backgroundLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.errorContainerDark
import com.rodcollab.gymmateapp.ui.theme.errorContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.errorContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.errorContainerLight
import com.rodcollab.gymmateapp.ui.theme.errorContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.errorContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.errorDark
import com.rodcollab.gymmateapp.ui.theme.errorDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.errorDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.errorLight
import com.rodcollab.gymmateapp.ui.theme.errorLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.errorLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.inverseOnSurfaceDark
import com.rodcollab.gymmateapp.ui.theme.inverseOnSurfaceDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.inverseOnSurfaceDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.inverseOnSurfaceLight
import com.rodcollab.gymmateapp.ui.theme.inverseOnSurfaceLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.inverseOnSurfaceLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.inversePrimaryDark
import com.rodcollab.gymmateapp.ui.theme.inversePrimaryDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.inversePrimaryDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.inversePrimaryLight
import com.rodcollab.gymmateapp.ui.theme.inversePrimaryLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.inversePrimaryLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.inverseSurfaceDark
import com.rodcollab.gymmateapp.ui.theme.inverseSurfaceDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.inverseSurfaceDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.inverseSurfaceLight
import com.rodcollab.gymmateapp.ui.theme.inverseSurfaceLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.inverseSurfaceLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onBackgroundDark
import com.rodcollab.gymmateapp.ui.theme.onBackgroundDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onBackgroundDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onBackgroundLight
import com.rodcollab.gymmateapp.ui.theme.onBackgroundLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onBackgroundLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onErrorContainerDark
import com.rodcollab.gymmateapp.ui.theme.onErrorContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onErrorContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onErrorContainerLight
import com.rodcollab.gymmateapp.ui.theme.onErrorContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onErrorContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onErrorDark
import com.rodcollab.gymmateapp.ui.theme.onErrorDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onErrorDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onErrorLight
import com.rodcollab.gymmateapp.ui.theme.onErrorLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onErrorLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onPrimaryContainerDark
import com.rodcollab.gymmateapp.ui.theme.onPrimaryContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onPrimaryContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onPrimaryContainerLight
import com.rodcollab.gymmateapp.ui.theme.onPrimaryContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onPrimaryContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onPrimaryDark
import com.rodcollab.gymmateapp.ui.theme.onPrimaryDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onPrimaryDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onPrimaryLight
import com.rodcollab.gymmateapp.ui.theme.onPrimaryLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onPrimaryLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onSecondaryContainerDark
import com.rodcollab.gymmateapp.ui.theme.onSecondaryContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onSecondaryContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onSecondaryContainerLight
import com.rodcollab.gymmateapp.ui.theme.onSecondaryContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onSecondaryContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onSecondaryDark
import com.rodcollab.gymmateapp.ui.theme.onSecondaryDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onSecondaryDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onSecondaryLight
import com.rodcollab.gymmateapp.ui.theme.onSecondaryLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onSecondaryLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onSurfaceDark
import com.rodcollab.gymmateapp.ui.theme.onSurfaceDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onSurfaceDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onSurfaceLight
import com.rodcollab.gymmateapp.ui.theme.onSurfaceLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onSurfaceLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onSurfaceVariantDark
import com.rodcollab.gymmateapp.ui.theme.onSurfaceVariantDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onSurfaceVariantDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onSurfaceVariantLight
import com.rodcollab.gymmateapp.ui.theme.onSurfaceVariantLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onSurfaceVariantLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onTertiaryContainerDark
import com.rodcollab.gymmateapp.ui.theme.onTertiaryContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onTertiaryContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onTertiaryContainerLight
import com.rodcollab.gymmateapp.ui.theme.onTertiaryContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onTertiaryContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onTertiaryDark
import com.rodcollab.gymmateapp.ui.theme.onTertiaryDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.onTertiaryDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.onTertiaryLight
import com.rodcollab.gymmateapp.ui.theme.onTertiaryLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.onTertiaryLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.outlineDark
import com.rodcollab.gymmateapp.ui.theme.outlineDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.outlineDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.outlineLight
import com.rodcollab.gymmateapp.ui.theme.outlineLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.outlineLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.outlineVariantDark
import com.rodcollab.gymmateapp.ui.theme.outlineVariantDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.outlineVariantDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.outlineVariantLight
import com.rodcollab.gymmateapp.ui.theme.outlineVariantLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.outlineVariantLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.primaryContainerDark
import com.rodcollab.gymmateapp.ui.theme.primaryContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.primaryContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.primaryContainerLight
import com.rodcollab.gymmateapp.ui.theme.primaryContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.primaryContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.primaryDark
import com.rodcollab.gymmateapp.ui.theme.primaryDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.primaryDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.primaryLight
import com.rodcollab.gymmateapp.ui.theme.primaryLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.primaryLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.scrimDark
import com.rodcollab.gymmateapp.ui.theme.scrimDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.scrimDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.scrimLight
import com.rodcollab.gymmateapp.ui.theme.scrimLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.scrimLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.secondaryContainerDark
import com.rodcollab.gymmateapp.ui.theme.secondaryContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.secondaryContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.secondaryContainerLight
import com.rodcollab.gymmateapp.ui.theme.secondaryContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.secondaryContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.secondaryDark
import com.rodcollab.gymmateapp.ui.theme.secondaryDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.secondaryDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.secondaryLight
import com.rodcollab.gymmateapp.ui.theme.secondaryLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.secondaryLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceBrightDark
import com.rodcollab.gymmateapp.ui.theme.surfaceBrightDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceBrightDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceBrightLight
import com.rodcollab.gymmateapp.ui.theme.surfaceBrightLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceBrightLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerDark
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighDark
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighLight
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighestDark
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighestDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighestDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighestLight
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighestLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerHighestLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLight
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowDark
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowLight
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowestDark
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowestDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowestDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowestLight
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowestLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceContainerLowestLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceDark
import com.rodcollab.gymmateapp.ui.theme.surfaceDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceDimDark
import com.rodcollab.gymmateapp.ui.theme.surfaceDimDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceDimDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceDimLight
import com.rodcollab.gymmateapp.ui.theme.surfaceDimLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceDimLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceLight
import com.rodcollab.gymmateapp.ui.theme.surfaceLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceVariantDark
import com.rodcollab.gymmateapp.ui.theme.surfaceVariantDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceVariantDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceVariantLight
import com.rodcollab.gymmateapp.ui.theme.surfaceVariantLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.surfaceVariantLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.tertiaryContainerDark
import com.rodcollab.gymmateapp.ui.theme.tertiaryContainerDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.tertiaryContainerDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.tertiaryContainerLight
import com.rodcollab.gymmateapp.ui.theme.tertiaryContainerLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.tertiaryContainerLightMediumContrast
import com.rodcollab.gymmateapp.ui.theme.tertiaryDark
import com.rodcollab.gymmateapp.ui.theme.tertiaryDarkHighContrast
import com.rodcollab.gymmateapp.ui.theme.tertiaryDarkMediumContrast
import com.rodcollab.gymmateapp.ui.theme.tertiaryLight
import com.rodcollab.gymmateapp.ui.theme.tertiaryLightHighContrast
import com.rodcollab.gymmateapp.ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun GymMateAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      
      darkTheme -> darkScheme
      else -> lightScheme
  }
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.surface.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

