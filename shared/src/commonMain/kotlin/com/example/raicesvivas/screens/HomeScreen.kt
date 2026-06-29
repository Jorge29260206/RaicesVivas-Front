package com.example.raicesvivas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.LenguaDto
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.theme.*

@Composable
fun HomeScreen(sesion: SesionUsuario?, lenguaActual: LenguaDto?, onAprender: () -> Unit, onDiccionario: () -> Unit, onPerfil: () -> Unit, onCambiarLengua: () -> Unit) {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                listOf(Pair("Inicio","🏠"), Pair("Aprender","📚"), Pair("Diccionario","📖"), Pair("Perfil","👤")).forEachIndexed { index, (label, icon) ->
                    NavigationBarItem(
                        selected = index == 0,
                        onClick = { when(index) { 1 -> onAprender(); 2 -> onDiccionario(); 3 -> onPerfil() } },
                        icon = { Text(icon, fontSize = 20.sp) },
                        label = { Text(label, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Verde.copy(alpha = 0.2f))
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(BeigeCalido).padding(padding).padding(24.dp)) {
            item {
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Hola, ${sesion?.nombreUsuario ?: "Usuario"}!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                        Text("Que bueno verte de nuevo", fontSize = 14.sp, color = CafeTierra.copy(alpha = 0.6f))
                    }
                    Box(modifier = Modifier.size(48.dp).background(Verde, CircleShape), contentAlignment = Alignment.Center) {
                        Text(sesion?.nombreUsuario?.firstOrNull()?.uppercase() ?: "U", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Spacer(Modifier.height(20.dp))
                lenguaActual?.let { lengua ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Verde)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Aprendiendo", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                                Text(lengua.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(lengua.region, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Text("🌿", fontSize = 40.sp)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Racha diaria", fontSize = 13.sp, color = GrisSuave)
                            Text("7 dias", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                        }
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(progress = { 0.65f }, modifier = Modifier.size(64.dp), color = Verde, trackColor = GrisSuave.copy(alpha = 0.3f), strokeWidth = 6.dp)
                            Text("65%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Que quieres hacer hoy?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = CafeTierra)
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Verde), onClick = onAprender) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("📚", fontSize = 28.sp); Spacer(Modifier.height(8.dp))
                            Text("Aprender", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                            Text("Practica tu lengua", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Terracota), onClick = {}) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("🎤", fontSize = 28.sp); Spacer(Modifier.height(8.dp))
                            Text("Grabar", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                            Text("Comparte tu voz", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Turquesa), onClick = onDiccionario) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("📖", fontSize = 28.sp); Spacer(Modifier.height(8.dp))
                            Text("Diccionario", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                            Text("Explora palabras", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CafeTierra), onClick = onPerfil) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("👤", fontSize = 28.sp); Spacer(Modifier.height(8.dp))
                            Text("Perfil", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                            Text("Tu progreso", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = BeigeMaiz.copy(alpha = 0.5f))) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Palabra del dia", fontSize = 13.sp, color = GrisSuave, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        Text("Tlalli", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                        Text("Tierra", fontSize = 18.sp, color = Turquesa, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        Text("Nahuatl", fontSize = 12.sp, color = GrisSuave)
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = onCambiarLengua, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = CafeTierra)) {
                    Text("Cambiar lengua", fontSize = 14.sp)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}