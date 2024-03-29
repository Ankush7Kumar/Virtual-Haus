package com.example.virtualhaus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DarkColorPalette = darkColors(
        primary = Purple200,
        primaryVariant = Purple700,
        secondary = Teal200
)

private val LightColorPalette = lightColors(
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = Teal200

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun VirtualHausTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    /*
    val colors = if (darkTheme) {\
        DarkColorPalette
    } else {
        LightColorPalette
    }
    */

    val colors = lightColors(
        primary = BluePrimary,
        primaryVariant = BlueSecondary,
        secondary = Orange200
    )

    val Typography = Typography(
        h1 = TextStyle(
            fontFamily = HeaderFont,
            fontWeight = FontWeight.W300,
            fontSize = 48.sp
        ),
        body1 = TextStyle(
            fontFamily = BodyFont,
            fontWeight = FontWeight.W300,
            fontSize = 24.sp
        )
    )

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}