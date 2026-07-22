package com.example.raicesvivas.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.theme.*
import com.example.raicesvivas.utils.MascotaHelper

@Composable
fun SplashScreen() {
    val mascota = remember { MascotaHelper.mascotaAleatoria() }
    Box(
        modifier = Modifier.fillMaxSize().background(BeigeCalido),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val resId = context.resources.getIdentifier(mascota, "drawable", context.packageName)
            if (resId != 0) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = "Xolo mascota",
                    modifier = Modifier.size(200.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
            Text("RaicesVivas", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Verde)
            Text("Preservando voces,", fontSize = 16.sp, color = CafeTierra)
            Text("conectando generaciones.", fontSize = 16.sp, color = CafeTierra)
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator(color = Verde, modifier = Modifier.size(32.dp), strokeWidth = 3.dp)
        }
    }
}

@Composable
fun OnboardingScreen(onSiguiente: () -> Unit) {
    val mascota = remember { MascotaHelper.mascotaAleatoria() }
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val resId = context.resources.getIdentifier(mascota, "drawable", context.packageName)
            if (resId != 0) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = "Xolo mascota",
                    modifier = Modifier.size(220.dp)
                )
            }
            Spacer(Modifier.height(32.dp))
            Text("Tu lengua\nes tu historia", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTierra, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Text("Cada palabra guarda el conocimiento\nde nuestros ancestros y la sabiduria\nde nuestra gente.", fontSize = 16.sp, color = CafeTierra.copy(alpha = 0.7f), textAlign = TextAlign.Center)
            Spacer(Modifier.height(48.dp))
            Button(
                onClick = onSiguiente,
                colors = ButtonDefaults.buttonColors(containerColor = Verde),
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Text("→", fontSize = 24.sp, color = Color.White)
            }
        }
        TextButton(onClick = onSiguiente, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
            Text("Saltar", color = Turquesa, fontSize = 16.sp)
        }
    }
}

@Composable
fun LoginScreen(onCrearCuenta: () -> Unit, onEntrar: () -> Unit) {
    val mascota = remember { MascotaHelper.mascotaAleatoria() }
    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val resId = context.resources.getIdentifier(mascota, "drawable", context.packageName)
            if (resId != 0) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = "Xolo mascota",
                    modifier = Modifier.size(160.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text("RaicesVivas", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Verde)
            Text("Preservando voces, conectando generaciones.", fontSize = 13.sp, color = CafeTierra.copy(alpha = 0.7f), textAlign = TextAlign.Center)
            Spacer(Modifier.height(48.dp))
            Text("Inicia sesion o crea tu cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onCrearCuenta, colors = ButtonDefaults.buttonColors(containerColor = Verde), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Crear cuenta", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onEntrar, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = CafeTierra)) {
                Text("Entrar", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun TopBarConRegreso(titulo: String, onVolver: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = onVolver) { Text("←", fontSize = 20.sp, color = CafeTierra) }
        Spacer(Modifier.width(8.dp))
        Text(titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
    }
}