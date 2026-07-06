package com.example.raicesvivas

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.raicesvivas.repository.RaicesRepository
import com.example.raicesvivas.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {

    private val solicitarPermisoNotif = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) NotificacionHelper.programarRecordatorioDiario(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CloudinaryConfig.inicializar(this)
        NotificacionHelper.crearCanal(this)
        solicitarPermisoNotificaciones()

        setContent {
            var mostrarDialogoFoto by remember { mutableStateOf(false) }
            var sesionId by remember { mutableStateOf<Int?>(null) }
            val repo = remember { RaicesRepository() }

            val fotoUri = remember {
                val archivo = File(filesDir, "foto_temp.jpg")
                FileProvider.getUriForFile(this, "$packageName.provider", archivo)
            }

            val launcherGaleria = rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri: Uri? ->
                uri?.let { subirFoto(it.toString(), sesionId, repo) }
            }

            val launcherCamara = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicture()
            ) { exito ->
                if (exito) subirFoto(fotoUri.toString(), sesionId, repo)
            }

            if (mostrarDialogoFoto) {
                Dialog(onDismissRequest = { mostrarDialogoFoto = false }) {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Cambiar foto de perfil", fontSize = 18.sp, color = CafeTierra)
                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = {
                                    mostrarDialogoFoto = false
                                    launcherCamara.launch(fotoUri)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Verde),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("📷  Tomar foto", color = Color.White)
                            }
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    mostrarDialogoFoto = false
                                    launcherGaleria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Turquesa),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("🖼️  Elegir de galeria", color = Color.White)
                            }
                            Spacer(Modifier.height(8.dp))
                            TextButton(onClick = { mostrarDialogoFoto = false }) {
                                Text("Cancelar", color = GrisSuave)
                            }
                        }
                    }
                }
            }

            App(onSolicitarFoto = { mostrarDialogoFoto = true })
        }
    }

    private fun subirFoto(uri: String, usuarioId: Int?, repo: RaicesRepository) {
        CloudinaryConfig.subirFoto(
            rutaArchivo = uri,
            onExito = { url ->
                usuarioId?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        repo.actualizarFoto(it, url)
                    }
                }
            },
            onError = { error ->
                android.util.Log.e("FOTO", "Error subiendo foto: $error")
            }
        )
    }

    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    NotificacionHelper.programarRecordatorioDiario(this)
                }
                else -> solicitarPermisoNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            NotificacionHelper.programarRecordatorioDiario(this)
        }
    }
}