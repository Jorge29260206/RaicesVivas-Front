package com.example.raicesvivas.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.LenguaDto
import com.example.raicesvivas.repository.RaicesRepository
import com.example.raicesvivas.theme.*

@Composable
fun SeleccionLenguaScreen(onLenguaSeleccionada: (LenguaDto) -> Unit, onVolver: () -> Unit) {
    val repo = remember { RaicesRepository() }
    var lenguas by remember { mutableStateOf<List<LenguaDto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try { lenguas = repo.getLenguas() } catch (e: Exception) { }
        cargando = false
    }

    BackHandler { onVolver() }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                TopBarConRegreso("Elige tu lengua", onVolver)
                Text("Descarga el paquete para usarlo sin conexion.", fontSize = 14.sp, color = CafeTierra.copy(alpha = 0.7f), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                if (cargando) { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Verde) } }
            }
            items(lenguas) { lengua ->
                Card(onClick = { onLenguaSeleccionada(lengua) }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(48.dp).background(Verde.copy(alpha = 0.15f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                            Text("🌿", fontSize = 24.sp)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(lengua.nombre, fontWeight = FontWeight.Bold, color = CafeTierra, fontSize = 16.sp)
                            Text(lengua.region, color = GrisSuave, fontSize = 13.sp)
                            lengua.descripcion?.let { Text(it, color = CafeTierra.copy(alpha = 0.6f), fontSize = 11.sp, maxLines = 1) }
                        }
                        Text("→", color = Verde, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}