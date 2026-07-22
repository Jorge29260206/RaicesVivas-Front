package com.example.raicesvivas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.Logro
import com.example.raicesvivas.models.LogrosData
import com.example.raicesvivas.theme.*

enum class FiltroLogro { TODOS, DESBLOQUEADOS, BLOQUEADOS }

@Composable
fun LogrosScreen(
    palabrasAprendidas: Int = 0,
    audiosGrabados: Int = 0,
    rachaDias: Int = 0,
    categoriasCompletadas: Int = 0,
    onVolver: () -> Unit
) {
    val logros = remember { LogrosData.listaCompleta(palabrasAprendidas, audiosGrabados, rachaDias, categoriasCompletadas) }
    var filtro by remember { mutableStateOf(FiltroLogro.TODOS) }
    val desbloqueados = logros.count { it.desbloqueado }

    val logrosFiltrados = when (filtro) {
        FiltroLogro.TODOS -> logros
        FiltroLogro.DESBLOQUEADOS -> logros.filter { it.desbloqueado }
        FiltroLogro.BLOQUEADOS -> logros.filter { !it.desbloqueado }
    }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onVolver) { Text("←", fontSize = 22.sp, color = CafeTierra) }
                    Text("Mis logros", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                }
                Text("🏆", fontSize = 22.sp)
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                // Tarjeta principal tipo "Guardian de la lengua"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Verde)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🛡️", fontSize = 48.sp)
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Guardián de la lengua",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Sigue aprendiendo y preservando tu herencia.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { if (logros.isNotEmpty()) desbloqueados / logros.size.toFloat() else 0f },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.3f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "$desbloqueados/${logros.size}",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Tabs de filtro
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FiltroTab("Todos", filtro == FiltroLogro.TODOS) { filtro = FiltroLogro.TODOS }
                    FiltroTab("Desbloqueados", filtro == FiltroLogro.DESBLOQUEADOS) { filtro = FiltroLogro.DESBLOQUEADOS }
                    FiltroTab("Bloqueados", filtro == FiltroLogro.BLOQUEADOS) { filtro = FiltroLogro.BLOQUEADOS }
                }
                Spacer(Modifier.height(16.dp))
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(logrosFiltrados) { logro ->
                    LogroCard(logro)
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun RowScope.FiltroTab(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) Verde else Color.White
        )
    ) {
        Text(
            texto,
            modifier = Modifier.padding(vertical = 10.dp),
            fontSize = 12.sp,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            color = if (seleccionado) Color.White else GrisSuave,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun LogroCard(logro: Logro) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (logro.desbloqueado) Color.White else GrisSuave.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(
                    if (logro.desbloqueado) Verde.copy(alpha = 0.15f) else GrisBloqueado.copy(alpha = 0.3f),
                    RoundedCornerShape(14.dp)
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(if (logro.desbloqueado) logro.emoji else "🔒", fontSize = 22.sp)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                logro.titulo,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (logro.desbloqueado) CafeTierra else GrisBloqueado,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
