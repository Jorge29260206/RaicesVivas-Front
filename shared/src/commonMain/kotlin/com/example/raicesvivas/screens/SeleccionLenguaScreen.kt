package com.example.raicesvivas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.LenguaDto
import com.example.raicesvivas.models.Resultado
import com.example.raicesvivas.repository.RaicesRepository
import com.example.raicesvivas.theme.*
import com.example.raicesvivas.utils.RegionHelper

@Composable
fun SeleccionLenguaScreen(
    onLenguaSeleccionada: (LenguaDto) -> Unit,
    onVolver: () -> Unit,
    sugerenciaGPS: RegionHelper.SugerenciaGPS? = null
) {
    val repo = remember { RaicesRepository() }
    var lenguas by remember { mutableStateOf<List<LenguaDto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var busqueda by remember { mutableStateOf("") }
    var sugerenciaLocal by remember { mutableStateOf(sugerenciaGPS) }
    var buscandoGPS by remember { mutableStateOf(sugerenciaGPS == null) }

    LaunchedEffect(sugerenciaGPS) {
        if (sugerenciaGPS != null) {
            sugerenciaLocal = sugerenciaGPS
            buscandoGPS = false
        }
    }

    LaunchedEffect(Unit) {
        when (val r = repo.getLenguas()) {
            is Resultado.Exito -> lenguas = r.datos
            is Resultado.Error -> error = r.mensaje
            Resultado.Cargando -> {}
        }
        cargando = false
        // Si despues de cargar sigue sin GPS, dejamos de esperar
        kotlinx.coroutines.delay(5000)
        buscandoGPS = false
    }

    val lenguasFiltradas = lenguas.filter {
        busqueda.isBlank() ||
        it.nombre.contains(busqueda, ignoreCase = true) ||
        it.region.contains(busqueda, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                TopBarConRegreso("Elige tu lengua", onVolver)
                Spacer(Modifier.height(8.dp))

                // Barra de busqueda
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    label = { Text("Buscar lengua o region...") },
                    leadingIcon = { Text("🔍", fontSize = 18.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Verde,
                        unfocusedBorderColor = GrisSuave.copy(alpha = 0.5f)
                    )
                )
                Spacer(Modifier.height(12.dp))

                // Tarjeta GPS
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (sugerenciaLocal?.tienesSugerencia == true)
                            Verde.copy(alpha = 0.12f)
                        else
                            GrisSuave.copy(alpha = 0.15f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (buscandoGPS) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = Verde,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("📍", fontSize = 28.sp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            if (buscandoGPS) {
                                Text(
                                    "Obteniendo tu ubicacion...",
                                    fontSize = 13.sp,
                                    color = GrisSuave,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Detectando la lengua de tu region",
                                    fontSize = 12.sp,
                                    color = GrisSuave.copy(alpha = 0.7f)
                                )
                            } else if (sugerenciaLocal?.tienesSugerencia == true) {
                                Text(
                                    "Ubicacion detectada: ",
                                    fontSize = 12.sp,
                                    color = Verde,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    sugerenciaLocal?.mensaje ?: "",
                                    fontSize = 13.sp,
                                    color = CafeTierra
                                )
                            } else {
                                Text(
                                    "Ubicacion no detectada",
                                    fontSize = 13.sp,
                                    color = GrisSuave,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Elige la lengua que quieras aprender",
                                    fontSize = 12.sp,
                                    color = GrisSuave.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                if (cargando) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Verde)
                    }
                }
                error?.let {
                    Text(it, color = Terracota, fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                }

                if (!cargando && lenguasFiltradas.isEmpty() && busqueda.isNotEmpty()) {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 40.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("No se encontro \"\"",
                                color = GrisSuave, fontSize = 14.sp,
                                textAlign = TextAlign.Center)
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }

            items(lenguasFiltradas) { lengua ->
                val esSugerida = sugerenciaLocal?.tienesSugerencia == true &&
                    sugerenciaLocal?.lenguaSugerida?.equals(lengua.nombre, ignoreCase = true) == true

                Card(
                    onClick = { onLenguaSeleccionada(lengua) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (esSugerida) Verde.copy(alpha = 0.12f) else Color.White
                    ),
                    border = if (esSugerida) CardDefaults.outlinedCardBorder() else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(52.dp).background(
                                if (esSugerida) Verde.copy(alpha = 0.25f)
                                else Verde.copy(alpha = 0.12f),
                                RoundedCornerShape(14.dp)
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (esSugerida) "📍" else "🌿",
                                fontSize = 26.sp
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    lengua.nombre,
                                    fontWeight = FontWeight.Bold,
                                    color = CafeTierra,
                                    fontSize = 16.sp
                                )
                                if (esSugerida) {
                                    Spacer(Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Verde
                                    ) {
                                        Text(
                                            "Tu region",
                                            fontSize = 10.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(lengua.region, color = GrisSuave, fontSize = 13.sp)
                            lengua.descripcion?.let {
                                Text(
                                    it,
                                    color = CafeTierra.copy(alpha = 0.6f),
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                        Text("->", color = Verde, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}