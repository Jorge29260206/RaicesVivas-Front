package com.example.raicesvivas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.theme.*

@Composable
fun ConfiguracionScreen(
    sesion: SesionUsuario?,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit,
) {
    var notificacionesDiarias by remember { mutableStateOf(value = true) }
    var efectosSonido by remember { mutableStateOf(value = true) }
    var modoOscuro by remember { mutableStateOf(value = false) }
    var metaDiaria by remember { mutableStateOf(value = "10 min") }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                TopBarConRegreso("Configuración", onVolver)
                Spacer(Modifier.height(16.dp))

                // Sección Cuenta
                Text("Cuenta", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Verde, modifier = Modifier.padding(bottom = 8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ConfigInfoItem("Nombre de usuario", "@${sesion?.nombreUsuario ?: ""}")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisSuave.copy(alpha = 0.3f))
                        ConfigInfoItem("Correo", "usuario@ejemplo.com") // Idealmente vendría en la sesión
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Sección Preferencias de aprendizaje
                Text("Preferencias de aprendizaje", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Verde, modifier = Modifier.padding(bottom = 8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ConfigSwitchItem("Notificaciones diarias", "Recordatorios de racha", notificacionesDiarias) { notificacionesDiarias = it }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisSuave.copy(alpha = 0.3f))
                        ConfigSwitchItem("Efectos de sonido", "Sonidos al responder", efectosSonido) { efectosSonido = it }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisSuave.copy(alpha = 0.3f))
                        ConfigSelectItem("Meta diaria", metaDiaria) { /* Abrir selector */ }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Sección Aplicación
                Text("Aplicación", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Verde, modifier = Modifier.padding(bottom = 8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ConfigSwitchItem("Modo oscuro", "Cansa menos la vista", modoOscuro) { modoOscuro = it }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisSuave.copy(alpha = 0.3f))
                        ConfigActionItem("Centro de ayuda", "Preguntas frecuentes") { }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisSuave.copy(alpha = 0.3f))
                        ConfigActionItem("Privacidad y términos", "Información legal") { }
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Botón Cerrar Sesión (Copia del perfil por utilidad)
                OutlinedButton(
                    onClick = onCerrarSesion,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Terracota),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = androidx.compose.ui.graphics.SolidColor(Terracota))
                ) {
                    Text("Cerrar sesión", fontWeight = FontWeight.Bold)
                }
                
                Spacer(Modifier.height(16.dp))
                
                Text(
                    "Versión 1.0.0 (Beta)",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 12.sp,
                    color = GrisSuave
                )
                
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ConfigInfoItem(label: String, valor: String) {
    Column {
        Text(label, fontSize = 12.sp, color = GrisSuave)
        Text(valor, fontSize = 16.sp, color = CafeTierra, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ConfigSwitchItem(titulo: String, subtitulo: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontSize = 16.sp, color = CafeTierra, fontWeight = FontWeight.Medium)
            Text(subtitulo, fontSize = 12.sp, color = GrisSuave)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Verde)
        )
    }
}

@Composable
private fun ConfigSelectItem(titulo: String, valor: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(titulo, fontSize = 16.sp, color = CafeTierra, fontWeight = FontWeight.Medium)
        TextButton(onClick = onClick) {
            Text(valor, color = Turquesa, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(4.dp))
            Text("→", color = Turquesa)
        }
    }
}

@Composable
private fun ConfigActionItem(titulo: String, subtitulo: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(titulo, fontSize = 16.sp, color = CafeTierra, fontWeight = FontWeight.Medium)
            Text(subtitulo, fontSize = 12.sp, color = GrisSuave)
        }
        IconButton(onClick = onClick) {
            Text("→", color = GrisSuave, fontSize = 18.sp)
        }
    }
}
