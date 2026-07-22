package com.example.raicesvivas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.example.raicesvivas.theme.*
import com.example.raicesvivas.models.LenguaDto

data class RegionMapa(
    val nombre: String,
    val lengua: String,
    val latLng: LatLng,
    val color: Long,
    val emoji: String,
    val descripcion: String
)

val REGIONES_MAPA = listOf(
    RegionMapa("Centro de Mexico", "Nahuatl",   LatLng(19.4326, -99.1332), 0xFF2E6B47, "🌿", "Ciudad de Mexico, Hidalgo, Puebla, Veracruz"),
    RegionMapa("Peninsula de Yucatan", "Maya",  LatLng(20.9674, -89.6237), 0xFF185FA5, "🔵", "Yucatan, Campeche, Quintana Roo, Chiapas"),
    RegionMapa("Oaxaca", "Zapoteco",            LatLng(17.0732, -96.7266), 0xFFC65D3B, "🟠", "Valles Centrales de Oaxaca"),
    RegionMapa("Oaxaca / Guerrero", "Mixteco",  LatLng(17.5500, -97.9200), 0xFFB71C1C, "🔴", "Oaxaca, Guerrero y Puebla"),
    RegionMapa("Queretaro / Hidalgo", "Otomi",  LatLng(20.5881, -100.3899), 0xFF6A1B9A, "🟣", "Queretaro, Hidalgo y Estado de Mexico"),
    RegionMapa("Michoacan", "Purepecha",        LatLng(19.7008, -101.1844), 0xFFF57F17, "🟡", "Region Purepecha de Michoacan"),
)

@Composable
fun MapaLenguasScreen(
    onLenguaSeleccionada: (String) -> Unit,
    onVolver: () -> Unit,
    lenguaActual: String? = null
) {
    var regionSeleccionada by remember { mutableStateOf<RegionMapa?>(null) }
    val mexicoCenter = LatLng(23.6345, -102.5528)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mexicoCenter, 4.5f)
    }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // TopBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onVolver) {
                    Text("<-", fontSize = 20.sp, color = CafeTierra)
                }
                Spacer(Modifier.width(8.dp))
                Text("Mapa de lenguas", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold, color = CafeTierra)
            }

            Text(
                "Toca una region para ver su lengua indigena",
                fontSize = 13.sp,
                color = GrisSuave,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(8.dp))

            // Mapa de Google Maps
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = false
                ),
                properties = MapProperties(mapType = MapType.NORMAL)
            ) {
                REGIONES_MAPA.forEach { region ->
                    val isSeleccionada = regionSeleccionada?.lengua == region.lengua
                    val isActual = lenguaActual?.equals(region.lengua, ignoreCase = true) == true

                    Marker(
                        state = MarkerState(position = region.latLng),
                        title = region.lengua,
                        snippet = region.descripcion,
                        onClick = {
                            regionSeleccionada = region
                            true
                        }
                    )

                    Circle(
                        center = region.latLng,
                        radius = if (isSeleccionada) 150000.0 else 100000.0,
                        fillColor = Color(region.color).copy(
                            alpha = if (isSeleccionada) 0.4f else if (isActual) 0.35f else 0.2f
                        ),
                        strokeColor = Color(region.color).copy(alpha = 0.8f),
                        strokeWidth = if (isSeleccionada || isActual) 4f else 2f
                    )
                }
            }

            // Panel inferior con info de region seleccionada
            regionSeleccionada?.let { region ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(region.emoji, fontSize = 36.sp)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(region.lengua,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CafeTierra)
                                Text(region.nombre,
                                    fontSize = 13.sp,
                                    color = GrisSuave)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(region.descripcion,
                            fontSize = 13.sp,
                            color = CafeTierra.copy(alpha = 0.7f))
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { onLenguaSeleccionada(region.lengua) },
                            colors = ButtonDefaults.buttonColors(containerColor = Verde),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text("Aprender ", color = Color.White,
                                fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } ?: Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Toca un circulo en el mapa para seleccionar una lengua",
                    fontSize = 13.sp,
                    color = GrisSuave,
                    modifier = Modifier.padding(vertical = 16.dp))
            }
        }
    }
}