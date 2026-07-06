package com.example.raicesvivas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val solicitarPermiso = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            NotificacionHelper.programarRecordatorioDiario(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CloudinaryConfig.inicializar(this)
        NotificacionHelper.crearCanal(this)
        solicitarPermisoNotificaciones()
        setContent {
            App()
        }
    }

    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    NotificacionHelper.programarRecordatorioDiario(this)
                }
                else -> solicitarPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            NotificacionHelper.programarRecordatorioDiario(this)
        }
    }
}