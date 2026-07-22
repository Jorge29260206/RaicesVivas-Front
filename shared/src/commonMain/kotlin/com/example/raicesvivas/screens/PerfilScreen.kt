package com.example.raicesvivas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.theme.*

@Composable
fun PerfilScreen(
    sesion: SesionUsuario?,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit,
    onCambiarFoto: () -> Unit = {},
    onLogros: () -> Unit = {},
    fotoUrl: String? = null,
    fotoContent: @Composable (Modifier) -> Unit = { modifier ->
        Box(modifier = modifier.background(Verde, CircleShape), contentAlignment = Alignment.Center) {
            Text(sesion?.nombreUsuario?.firstOrNull()?.uppercase() ?: "U", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
) {
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                TopBarConRegreso("Mi Perfil", onVolver)
                Spacer(Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.size(100.dp).clickable { onCambiarFoto() },
                            contentAlignment = Alignment.Center
                        ) {
                            fotoContent(Modifier.fillMaxSize().clip(CircleShape))
                            Box(
                                modifier = Modifier.align(Alignment.BottomEnd).size(28.dp).background(Terracota, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📷", fontSize = 14.sp)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(sesion?.nombreCompleto ?: "Usuario", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                        Text("@${sesion?.nombreUsuario ?: ""}", fontSize = 14.sp, color = GrisSuave)
                        Text("Toca la foto para cambiarla", fontSize = 11.sp, color = GrisSuave.copy(alpha = 0.7f))
                    }
                }
                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf(Triple("7","Racha","🔥"), Triple("3","Lecciones","📚"), Triple("85%","Promedio","⭐")).forEach { (valor, label, emoji) ->
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(emoji, fontSize = 24.sp)
                                Text(valor, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                                Text(label, fontSize = 11.sp, color = GrisSuave, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                
                PerfilItem("🏆", "Mis logros", onClick = onLogros)
                PerfilItem("📊", "Mi progreso")
                PerfilItem("⚙️", "Configuracion")
                PerfilItem("❓", "Ayuda")
                
                Spacer(Modifier.height(24.dp))
                Button(onClick = onCerrarSesion, colors = ButtonDefaults.buttonColors(containerColor = Terracota), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    Text("Cerrar sesion", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun PerfilItem(emoji: String, label: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 20.sp)
            Spacer(Modifier.width(16.dp))
            Text(label, fontSize = 16.sp, color = CafeTierra, modifier = Modifier.weight(1f))
            Text("→", color = GrisSuave, fontSize = 16.sp)
        }
    }
}
