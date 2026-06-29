package com.example.raicesvivas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.raicesvivas.theme.*

@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(120.dp).background(Verde, CircleShape), contentAlignment = Alignment.Center) {
                Text("RV", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.height(24.dp))
            Text("Raices Vivas", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
            Spacer(Modifier.height(8.dp))
            Text("Preservando voces,\nconectando generaciones.", fontSize = 14.sp, color = Turquesa, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Row {
                Box(Modifier.size(8.dp).background(Terracota, RoundedCornerShape(4.dp)))
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(8.dp).background(Verde, RoundedCornerShape(4.dp)))
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(8.dp).background(Terracota, RoundedCornerShape(4.dp)))
            }
        }
    }
}

@Composable
fun OnboardingScreen(onContinuar: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        TextButton(onClick = onContinuar, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
            Text("Saltar", color = Turquesa)
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.size(160.dp).background(Verde.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Text("🌿", fontSize = 72.sp)
            }
            Spacer(Modifier.height(32.dp))
            Text("Tu lengua\nes tu historia", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTierra, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Text("Aprende lenguas indigenas mexicanas de forma interactiva y divertida.", fontSize = 16.sp, color = CafeTierra.copy(alpha = 0.7f), textAlign = TextAlign.Center)
            Spacer(Modifier.height(48.dp))
            Button(onClick = onContinuar, colors = ButtonDefaults.buttonColors(containerColor = Verde), shape = RoundedCornerShape(50), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Comenzar", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LoginScreen(onCrearCuenta: () -> Unit, onEntrar: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.size(80.dp).background(Verde, CircleShape), contentAlignment = Alignment.Center) {
                Text("RV", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.height(16.dp))
            Text("Raices Vivas", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
            Spacer(Modifier.height(8.dp))
            Text("Preservando voces,\nconectando generaciones.", fontSize = 13.sp, color = Turquesa, textAlign = TextAlign.Center)
            Spacer(Modifier.height(48.dp))
            Button(onClick = onCrearCuenta, colors = ButtonDefaults.buttonColors(containerColor = Verde), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Crear cuenta", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onEntrar, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = CafeTierra)) {
                Text("Iniciar sesion", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Composable
fun TopBarConRegreso(titulo: String, onVolver: () -> Unit) {
    Row(modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(top = 40.dp, start = 4.dp, end = 16.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = onVolver) { Text("<", color = CafeTierra, fontSize = 22.sp, fontWeight = FontWeight.Bold) }
        Text(titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
    }
}
