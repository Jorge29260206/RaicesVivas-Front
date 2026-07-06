package com.example.raicesvivas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.Resultado
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.repository.RaicesRepository
import com.example.raicesvivas.theme.*
import com.example.raicesvivas.utils.validarCorreoLogin
import com.example.raicesvivas.utils.validarContrasenaLogin
import kotlinx.coroutines.launch

@Composable
fun EntrarScreen(onLoginExitoso: (SesionUsuario) -> Unit, onVolver: () -> Unit) {
    val repo = remember { RaicesRepository() }
    val scope = rememberCoroutineScope()
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var mensajeServidor by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            TopBarConRegreso("Iniciar sesion", onVolver)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = correo, onValueChange = { correo = it; errorCorreo = validarCorreoLogin(it) }, label    = { Text("Correo electronico") }, isError = errorCorreo != null, supportingText = { errorCorreo?.let { Text(it, color = Terracota, fontSize = 12.sp) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = contrasena, onValueChange = { contrasena = it; errorContrasena = validarContrasenaLogin(it) }, label = { Text("Contrasena") }, isError = errorContrasena != null, supportingText = { errorContrasena?.let { Text(it, color = Terracota, fontSize = 12.sp) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), visualTransformation = PasswordVisualTransformation())
            Spacer(Modifier.height(16.dp))
            if (mensajeServidor.isNotEmpty()) Text(mensajeServidor, color = Terracota, fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    errorCorreo = validarCorreoLogin(correo)
                    errorContrasena = validarContrasenaLogin(contrasena)
                    if (errorCorreo == null && errorContrasena == null) {
                        scope.launch {
                            cargando = true
                            mensajeServidor = ""
                            when (val resultado = repo.login(correo, contrasena)) {
                                is Resultado.Exito -> onLoginExitoso(resultado.datos)
                                is Resultado.Error -> mensajeServidor = resultado.mensaje
                                Resultado.Cargando -> {}
                            }
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
                else Text("Entrar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onVolver) { Text("No tengo cuenta, crear una", color = Turquesa) }
        }
    }
}