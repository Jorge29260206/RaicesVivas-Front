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
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.repository.RaicesRepository
import com.example.raicesvivas.theme.*
import com.example.raicesvivas.utils.RegionHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class MainActivity : ComponentActivity() {

    private val solicitarPermisoNotif = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) NotificacionHelper.programarRecordatorioDiario(this)
    }

    private val solicitarPermisoUbicacion = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val concedido = (permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        if (concedido) {
            obtenerUbicacionYSugerir()
        }
    }

    private var onSugerenciaObtenida: ((RegionHelper.SugerenciaGPS) -> Unit)? = null

    private fun obtenerUbicacionYSugerir() {
        val geoManager = GeolocalizacionManager(this)
        geoManager.obtenerUbicacion(
            onUbicacion = { lat, lon ->
                val sugerencia = RegionHelper.sugerirLengua(lat, lon)
                onSugerenciaObtenida?.invoke(sugerencia)
            },
            onError = { _ ->
                android.util.Log.e("GPS", "Error obteniendo ubicacion")
            }
        )
    }

    private fun copiarUriAArchivo(uri: Uri): File? {
        return try {
            val archivo = File(cacheDir, "foto_perfil_temp.jpg")
            contentResolver.openInputStream(uri)?.use { input ->
                archivo.outputStream().use { output -> input.copyTo(output) }
            }
            archivo
        } catch (e: Exception) {
            android.util.Log.e("FOTO", "Error copiando: ${e.message}")
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CloudinaryConfig.inicializar(this)
        NotificacionHelper.crearCanal(this)
        solicitarPermisoNotificaciones()

        val sesionGuardada = runBlocking { SesionDataStore.obtenerSesion(this@MainActivity) }
        val fotoGuardada = sesionGuardada?.fotoUrl

        setContent {
            val context = this@MainActivity
            val repo = remember { RaicesRepository() }
            var mostrarDialogoFoto by remember { mutableStateOf(false) }
            var sesionActual by remember { mutableStateOf<SesionUsuario?>(sesionGuardada) }
            var fotoUrlActual by remember { mutableStateOf<String?>(fotoGuardada) }
            var sugerenciaGPS by remember { mutableStateOf<RegionHelper.SugerenciaGPS?>(null) }

            // Conecta el callback de ubicacion al estado de Compose
            onSugerenciaObtenida = { sugerencia -> sugerenciaGPS = sugerencia }

            // Solicitar GPS al iniciar
            LaunchedEffect(Unit) {
                val tienePermiso = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                if (tienePermiso) {
                    obtenerUbicacionYSugerir()
                } else {
                    solicitarPermisoUbicacion.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }
            }

            val fotoUri = remember {
                val archivo = File(filesDir, "foto_temp.jpg")
                FileProvider.getUriForFile(context, "com.example.raicesvivas.provider", archivo)
            }

            val launcherGaleria = rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri: Uri? ->
                uri?.let { imageUri ->
                    val archivo = copiarUriAArchivo(imageUri)
                    if (archivo != null) {
                        CloudinaryConfig.subirFoto(
                            rutaArchivo = archivo.absolutePath,
                            onExito = { url ->
                                fotoUrlActual = url
                                sesionActual?.let { sesion ->
                                    CoroutineScope(Dispatchers.IO).launch {
                                        repo.actualizarFoto(sesion.id, url)
                                        SesionDataStore.guardarSesion(context, sesion.copy(fotoUrl = url))
                                    }
                                }
                            },
                            onError = { _ -> android.util.Log.e("FOTO", "Error en subida de galeria") }
                        )
                    }
                }
            }

            val launcherCamara = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicture()
            ) { exito ->
                if (exito) {
                    val archivo = File(filesDir, "foto_temp.jpg")
                    CloudinaryConfig.subirFoto(
                        rutaArchivo = archivo.absolutePath,
                        onExito = { url ->
                            fotoUrlActual = url
                            sesionActual?.let { sesion ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    repo.actualizarFoto(sesion.id, url)
                                    SesionDataStore.guardarSesion(context, sesion.copy(fotoUrl = url))
                                }
                            }
                        },
                        onError = { _ -> android.util.Log.e("FOTO", "Error en subida de camara") }
                    )
                }
            }

            if (mostrarDialogoFoto) {
                Dialog(onDismissRequest = { mostrarDialogoFoto = false }) {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Cambiar foto de perfil", fontSize = 18.sp, color = CafeTierra)
                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = { mostrarDialogoFoto = false; launcherCamara.launch(fotoUri) },
                                colors = ButtonDefaults.buttonColors(containerColor = Verde),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Tomar foto", color = Color.White) }
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { mostrarDialogoFoto = false; launcherGaleria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                                colors = ButtonDefaults.buttonColors(containerColor = Turquesa),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Elegir de galeria", color = Color.White) }
                            Spacer(Modifier.height(8.dp))
                            TextButton(onClick = { mostrarDialogoFoto = false }) { Text("Cancelar", color = GrisSuave) }
                        }
                    }
                }
            }

            App(
                sesionInicial = sesionGuardada,
                sugerenciaGPS = sugerenciaGPS,
                onSolicitarFoto = { mostrarDialogoFoto = true },
                onLoginExitoso = { sesion ->
                    sesionActual = sesion
                    CoroutineScope(Dispatchers.IO).launch {
                        SesionDataStore.guardarSesion(context, sesion)
                        NotificacionHelper.mostrarNotificacionBienvenida(context, sesion.nombreUsuario)
                    }
                },
                onCerrarSesion = {
                    sesionActual = null
                    fotoUrlActual = null
                    CoroutineScope(Dispatchers.IO).launch {
                        SesionDataStore.cerrarSesion(context)
                        NotificacionHelper.cancelarRecordatorio(context)
                    }
                },
                perfilContent = { sesion, onVolver, onCerrarSesion, onCambiarFoto ->
                    PerfilScreenAndroid(
                        sesion = sesion,
                        fotoUrl = fotoUrlActual,
                        onVolver = onVolver,
                        onCerrarSesion = onCerrarSesion,
                        onCambiarFoto = onCambiarFoto
                    )
                }
            )
        }
    }

    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                NotificacionHelper.programarRecordatorioDiario(this)
            } else {
                solicitarPermisoNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            NotificacionHelper.programarRecordatorioDiario(this)
        }
    }
}