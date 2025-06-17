package com.example.workoutplannerapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColorPalette = lightColors(
    primary = Color(0xFF81D4FA),
    primaryVariant = PurpleGrey40,
    secondary = Color(0xFF81D4FA),

    background = Color(0xFFF5F5F5),      // soft light grey
    surface = Color(0xFFFFFFFF),         // still white (for cards, dialogs)
    onBackground = Color(0xFF212121),    // dark grey for readability
    onSurface = Color.Black,       // softer than pure black

    onPrimary = Color.White,
    onSecondary = Color.White
)

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(6.dp),
    large = RoundedCornerShape(8.dp)
)

@Composable
fun WorkoutPlannerAppTheme(
    content: @Composable () -> Unit
) {
    val colors = LightColorPalette

    androidx.compose.material.MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
