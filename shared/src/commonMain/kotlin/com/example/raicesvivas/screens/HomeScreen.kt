package com.example.raicesvivas.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.LenguaDto
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.theme.*
import com.example.raicesvivas.utils.MascotaHelper

@Composable
fun HomeScreen(
    sesion: SesionUsuario?,
    lenguaActual: LenguaDto?,
    onAprender: () -> Unit,
    onDiccionario: () -> Unit,
    onPerfil: () -> Unit,
    onCambiarLengua: () -> Unit,
    fotoContent: @Composable (Modifier) -> Unit = { modifier ->
        Box(modifier = modifier.background(Verde, CircleShape), contentAlignment = Alignment.Center) {
            Text(sesion?.nombreUsuario?.firstOrNull()?.uppercase() ?: "U", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
) {
    val mascota = remember { MascotaHelper.mascotaAleatoria() }
    val mensajeMascota = remember { MascotaHelper.mensajeBienvenida(sesion?.nombreUsuario ?: "amigo") }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Hola, ${sesion?.nombreUsuario ?: "amigo"}!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                        Text("Que bueno verte de nuevo", fontSize = 14.sp, color = GrisSuave)
                    }
                    IconButton(onClick = onPerfil) {
                        fotoContent(Modifier.size(44.dp).clip(CircleShape))
                    }
                }
                Spacer(Modifier.height(16.dp))

                // Mascota con mensaje
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Verde.copy(alpha = 0.1f))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        val context = androidx.compose.ui.platform.LocalContext.current
                        val resId = context.resources.getIdentifier(mascota, "drawable", context.packageName)
                        if (resId != 0) {
                            Image(painter = painterResource(id = resId), contentDescription = "Xolo", modifier = Modifier.size(80.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Xolo dice:", fontSize = 12.sp, color = GrisSuave)
                            Text(mensajeMascota, fontSize = 14.sp, color = CafeTierra, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                // Lengua actual
                lenguaActual?.let {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("🌿", fontSize = 28.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text("Aprendiendo", fontSize = 12.sp, color = GrisSuave)
                                Text(it.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                            }
                            TextButton(onClick = onCambiarLengua) { Text("Cambiar", color = Turquesa, fontSize = 12.sp) }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // Botones principales
                Text("Que quieres hacer?", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(onClick = onAprender, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Verde)) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📚", fontSize = 32.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Aprender", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                            Text("Practica tu lengua", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                    Card(onClick = onDiccionario, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Turquesa)) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 32.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Diccionario", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                            Text("Explora palabras", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                if (lenguaActual == null) {
                    Card(onClick = onCambiarLengua, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Terracota)) {
                        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("🌎", fontSize = 32.sp)
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text("Elige tu lengua", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                                Text("Comienza tu viaje de aprendizaje", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            }
                            Text("→", color = Color.White, fontSize = 20.sp)
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
        NavigationBar(containerColor = Color.White, modifier = Modifier.align(Alignment.BottomCenter)) {
            NavigationBarItem(selected = true, onClick = {}, icon = { Text("🏠", fontSize = 20.sp) }, label = { Text("Inicio", fontSize = 11.sp) })
            NavigationBarItem(selected = false, onClick = onAprender, icon = { Text("📚", fontSize = 20.sp) }, label = { Text("Aprender", fontSize = 11.sp) })
            NavigationBarItem(selected = false, onClick = onDiccionario, icon = { Text("🔍", fontSize = 20.sp) }, label = { Text("Diccionario", fontSize = 11.sp) })
            NavigationBarItem(selected = false, onClick = onPerfil, icon = { Text("👤", fontSize = 20.sp) }, label = { Text("Perfil", fontSize = 11.sp) })
        }
    }
}