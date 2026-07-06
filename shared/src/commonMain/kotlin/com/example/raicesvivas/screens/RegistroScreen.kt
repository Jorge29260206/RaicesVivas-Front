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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.repository.RaicesRepository
import com.example.raicesvivas.theme.*
import com.example.raicesvivas.utils.*
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(onRegistrado: (SesionUsuario) -> Unit, onVolver: () -> Unit) {
    val repo = remember { RaicesRepository() }
    val scope = rememberCoroutineScope()
    var nombreCompleto by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var expandirPais by remember { mutableStateOf(false) }
    val paises = listOf("Mexico", "Colombia", "Argentina")
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorEdad by remember { mutableStateOf<String?>(null) }
    var errorPais by remember { mutableStateOf<String?>(null) }
    var mensajeServidor by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                TopBarConRegreso("Crear cuenta", onVolver)
                OutlinedTextField(value = nombreCompleto, onValueChange = { nombreCompleto = it; errorNombre = validarNombreCompleto(it) }, label = { Text("Nombre completo") }, isError = errorNombre != null, supportingText = { errorNombre?.let { Text(it, color = Terracota, fontSize = 12.sp) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(value = nombreUsuario, onValueChange = { nombreUsuario = it; errorUsuario = validarNombreUsuario(it) }, label = { Text("Nombre de usuario") }, isError = errorUsuario != null, supportingText = { errorUsuario?.let { Text(it, color = Terracota, fontSize = 12.sp) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(value = correo, onValueChange = { correo = it; errorCorreo = validarCorreo(it) }, label = { Text("Correo electronico") }, isError = errorCorreo != null, supportingText = { errorCorreo?.let { Text(it, color = Terracota, fontSize = 12.sp) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(value = contrasena, onValueChange = { contrasena = it; errorContrasena = validarContrasena(it) }, label = { Text("Contrasena") }, isError = errorContrasena != null, supportingText = { errorContrasena?.let { Text(it, color = Terracota, fontSize = 12.sp) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), visualTransformation = PasswordVisualTransformation())
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(value = edad, onValueChange = { edad = it; errorEdad = validarEdad(it) }, label = { Text("Edad") }, isError = errorEdad != null, supportingText = { errorEdad?.let { Text(it, color = Terracota, fontSize = 12.sp) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(value = pais, onValueChange = {}, readOnly = true, label = { Text("Pais") }, trailingIcon = { TextButton(onClick = { expandirPais = !expandirPais }) { Text("v", color = CafeTierra) } }, isError = errorPais != null, supportingText = { errorPais?.let { Text(it, color = Terracota, fontSize = 12.sp) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                if (expandirPais) {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        paises.forEach { p -> TextButton(onClick = { pais = p; errorPais = null; expandirPais = false }, modifier = Modifier.fillMaxWidth()) { Text(p, color = CafeTierra, modifier = Modifier.fillMaxWidth()) } }
                    }
                }
                Spacer(Modifier.height(12.dp))
                if (mensajeServidor.isNotEmpty()) Text(mensajeServidor, color = if (mensajeServidor.contains("error", true)) Terracota else Verde, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        errorNombre = validarNombreCompleto(nombreCompleto)
                        errorUsuario = validarNombreUsuario(nombreUsuario)
                        errorCorreo = validarCorreo(correo)
                        errorContrasena = validarContrasena(contrasena)
                        errorEdad = validarEdad(edad)
                        errorPais = validarPais(pais)
                        val hayErrores = listOf(errorNombre, errorUsuario, errorCorreo, errorContrasena, errorEdad, errorPais).any { it != null }
                        if (!hayErrores) {
                            scope.launch {
                                cargando = true
                                mensajeServidor = try {
                                    repo.registrar(nombreCompleto, nombreUsuario, correo, contrasena, edad.toIntOrNull() ?: 0, pais)
                                    onRegistrado(SesionUsuario(0, nombreUsuario, nombreCompleto))
                                    ""
                                } catch (e: Exception) { "Error: ${e.message}" }
                                cargando = false
                            }
                        }
                    },
                    enabled = !cargando,
                    colors = ButtonDefaults.buttonColors(containerColor = Verde),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    if (cargando) CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    else Text("Crear cuenta", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onVolver) { Text("Ya tengo cuenta", color = Turquesa) }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}