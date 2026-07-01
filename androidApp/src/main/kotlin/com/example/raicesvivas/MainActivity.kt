package com.example.raicesvivas

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import com.example.raicesvivas.models.SesionUsuario
import com.example.raicesvivas.repository.RaicesRepository
import com.example.raicesvivas.screens.PerfilScreen
import com.example.raicesvivas.theme.RaicesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CloudinaryConfig.inicializar(this)
        setContent {
            App()
        }
    }
}