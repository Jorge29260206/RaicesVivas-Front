package com.example.raicesvivas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.screens.PerfilScreen
import com.example.raicesvivas.theme.Verde

@androidx.compose.runtime.Composable
fun PerfilScreenAndroid(
    sesion: SesionUsuario?,
    fotoUrl: String?,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit,
    onCambiarFoto: () -> Unit
) {
    val context = LocalContext.current
    PerfilScreen(
        sesion = sesion,
        onVolver = onVolver,
        onCerrarSesion = onCerrarSesion,
        onCambiarFoto = onCambiarFoto,
        fotoUrl = fotoUrl,
        fotoContent = { modifier ->
            if (!fotoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(fotoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto de perfil",
                    modifier = modifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = modifier.background(Verde, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        sesion?.nombreUsuario?.firstOrNull()?.uppercase() ?: "U",
                        fontSize = androidx.compose.ui.unit.TextUnit(40f, androidx.compose.ui.unit.TextUnitType.Sp),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        }
    )
}