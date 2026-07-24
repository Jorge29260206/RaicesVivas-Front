package com.example.raicesvivas.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Verde = Color(0xFF2EBB57)
val Turquesa = Color(0xFF2AABA1)
val Terracota = Color(0xFFC65D3B)
val BeigeCalido = Color(0xFFFFFDF7)
val BeigeMaiz = Color(0xFFF4E382)
val CafeTierra = Color(0xFF6B4F3A)
val GrisSuave = Color(0xFFD9D3C7)
val Dorado = Color(0xFFD4A017)
val GrisBloqueado = Color(0xFFB8B0A0)

// Colores para tema oscuro
val DarkFondo = Color(0xFF1A1A1A)
val DarkSurface = Color(0xFF2D2D2D)
val DarkTexto = Color(0xFFE0E0E0)
val DarkTextoSecundario = Color(0xFFAAAAAA)

val RaicesLightColorScheme = lightColorScheme(
    primary = Verde,
    secondary = Turquesa,
    tertiary = Terracota,
    background = BeigeCalido,
    surface = BeigeCalido,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = CafeTierra,
    onSurface = CafeTierra,
)

val RaicesDarkColorScheme = darkColorScheme(
    primary = Verde,
    secondary = Turquesa,
    tertiary = Terracota,
    background = DarkFondo,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DarkTexto,
    onSurface = DarkTexto,
)

@Composable
fun RaicesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) RaicesDarkColorScheme else RaicesLightColorScheme
    MaterialTheme(colorScheme = colorScheme, content = content)
}
