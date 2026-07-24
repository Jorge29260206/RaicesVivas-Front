package com.example.raicesvivas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.*
import com.example.raicesvivas.screens.*
import com.example.raicesvivas.theme.RaicesTheme
import com.example.raicesvivas.utils.RegionHelper
import com.example.raicesvivas.utils.BackHandler

enum class Pantalla {
    SPLASH, ONBOARDING, LOGIN, ENTRAR, REGISTRO,
    SELECCION_LENGUA, MAPA_LENGUAS, HOME, APRENDER, LECCIONES,
    LECCION, RESULTADO, DICCIONARIO, PERFIL, LOGROS, CONFIGURACION
}

@Composable
fun App(
    sesionInicial: SesionUsuario? = null,
    sugerenciaGPS: RegionHelper.SugerenciaGPS? = null,
    onSolicitarFoto: (() -> Unit)? = null,
    onSolicitarGPS: (() -> Unit)? = null,
    onLoginExitoso: ((SesionUsuario) -> Unit)? = null,
    onCerrarSesion: (() -> Unit)? = null,
    fotoUrl: String? = null,
    fotoContent: (@Composable (Modifier) -> Unit)? = null,
    perfilContent: (@Composable (SesionUsuario?, () -> Unit, () -> Unit, () -> Unit, () -> Unit, () -> Unit) -> Unit)? = null,
    mapaContent: (@Composable ((String) -> Unit, () -> Unit, String?) -> Unit)? = null
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    
    RaicesTheme(darkTheme = isDarkTheme) {
        val stack = remember { mutableStateListOf<Pantalla>(if (sesionInicial != null) Pantalla.HOME else Pantalla.SPLASH) }
        val pantallaActual = stack.last()

        var sesion by remember { mutableStateOf(sesionInicial) }
        var lenguaSeleccionada by remember { mutableStateOf<LenguaDto?>(null) }
        var nivelSeleccionado by remember { mutableStateOf<NivelDto?>(null) }
        var leccionSeleccionada by remember { mutableStateOf<LeccionDto?>(null) }
        var puntuacionFinal by remember { mutableStateOf(0) }

        // Función para navegar hacia adelante
        val navegar: (Pantalla) -> Unit = { nuevaPantalla ->
            stack.add(nuevaPantalla)
        }

        // Función para volver atrás
        val volver: () -> Unit = {
            if (stack.size > 1) {
                stack.removeAt(stack.size - 1)
            }
        }

        // Manejador de gesto atrás nativo
        BackHandler(enabled = stack.size > 1) {
            volver()
        }

        if (sesionInicial == null && stack.size == 1 && stack.first() == Pantalla.SPLASH) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                stack.clear()
                stack.add(Pantalla.ONBOARDING)
            }
        }

        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                when (pantallaActual) {
                    Pantalla.SPLASH -> SplashScreen()
                    Pantalla.ONBOARDING -> OnboardingScreen {
                        stack.clear()
                        stack.add(Pantalla.LOGIN)
                    }
                    Pantalla.LOGIN -> LoginScreen(
                        onCrearCuenta = { navegar(Pantalla.REGISTRO) },
                        onEntrar = { navegar(Pantalla.ENTRAR) }
                    )
                    Pantalla.ENTRAR -> EntrarScreen(
                        onLoginExitoso = { s ->
                            sesion = s
                            stack.clear()
                            stack.add(Pantalla.HOME)
                            onLoginExitoso?.invoke(s)
                        },
                        onVolver = { volver() }
                    )
                    Pantalla.REGISTRO -> RegistroScreen(
                        onRegistrado = { s ->
                            sesion = s
                            stack.clear()
                            stack.add(Pantalla.SELECCION_LENGUA)
                            onLoginExitoso?.invoke(s)
                        },
                        onVolver = { volver() }
                    )
                    Pantalla.SELECCION_LENGUA -> SeleccionLenguaScreen(
                        onLenguaSeleccionada = { l -> 
                            lenguaSeleccionada = l
                            stack.clear()
                            stack.add(Pantalla.HOME)
                        },
                        onVolver = { if (sesion != null) volver() else { stack.clear(); stack.add(Pantalla.LOGIN) } },
                        sugerenciaGPS = sugerenciaGPS,
                        onVerMapa = { navegar(Pantalla.MAPA_LENGUAS) },
                        onReintentarGPS = { onSolicitarGPS?.invoke() }
                    )
                    Pantalla.MAPA_LENGUAS -> {
                        if (mapaContent != null) {
                            mapaContent(
                                { nombreLengua ->
                                    val lengua = LenguaDto(
                                        id = 0,
                                        nombre = nombreLengua,
                                        region = "",
                                        descripcion = null,
                                        activa = true
                                    )
                                    lenguaSeleccionada = lengua
                                    stack.clear()
                                    stack.add(Pantalla.HOME)
                                },
                                { volver() },
                                lenguaSeleccionada?.nombre
                            )
                        } else {
                            volver()
                        }
                    }
                    Pantalla.HOME -> HomeScreen(
                        sesion = sesion,
                        lenguaActual = lenguaSeleccionada,
                        onAprender = { navegar(Pantalla.APRENDER) },
                        onDiccionario = { navegar(Pantalla.DICCIONARIO) },
                        onPerfil = { navegar(Pantalla.PERFIL) },
                        onCambiarLengua = { navegar(Pantalla.SELECCION_LENGUA) },
                        fotoContent = fotoContent ?: { modifier ->
                            Box(modifier = modifier.background(com.example.raicesvivas.theme.Verde, CircleShape), contentAlignment = Alignment.Center) {
                                Text(sesion?.nombreUsuario?.firstOrNull()?.uppercase() ?: "U", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = androidx.compose.ui.graphics.Color.White)
                            }
                        }
                    )
                    Pantalla.APRENDER -> AprenderScreen(
                        lengua = lenguaSeleccionada,
                        onNivelSeleccionado = { n -> nivelSeleccionado = n; navegar(Pantalla.LECCIONES) },
                        onVolver = { volver() }
                    )
                    Pantalla.LECCIONES -> LeccionesScreen(
                        nivel = nivelSeleccionado,
                        onLeccionSeleccionada = { l -> leccionSeleccionada = l; navegar(Pantalla.LECCION) },
                        onVolver = { volver() }
                    )
                    Pantalla.LECCION -> LeccionScreen(
                        leccion = leccionSeleccionada,
                        sesion = sesion,
                        onTerminar = { p -> puntuacionFinal = p; stack.removeAt(stack.size - 1); navegar(Pantalla.RESULTADO) },
                        onVolver = { volver() }
                    )
                    Pantalla.RESULTADO -> ResultadoScreen(
                        puntuacion = puntuacionFinal,
                        onContinuar = { volver() },
                        onHome = { stack.clear(); stack.add(Pantalla.HOME) }
                    )
                    Pantalla.DICCIONARIO -> DiccionarioScreen(
                        lengua = lenguaSeleccionada,
                        onVolver = { volver() }
                    )
                    Pantalla.LOGROS -> LogrosScreen(
                        onVolver = { volver() }
                    )
                    Pantalla.CONFIGURACION -> ConfiguracionScreen(
                        sesion = sesion,
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it },
                        onVolver = { volver() },
                        onCerrarSesion = {
                            sesion = null
                            stack.clear()
                            stack.add(Pantalla.LOGIN)
                            onCerrarSesion?.invoke()
                        }
                    )
                    Pantalla.PERFIL -> {
                        if (perfilContent != null) {
                            perfilContent(
                                sesion,
                                { volver() },
                                { 
                                    sesion = null
                                    stack.clear()
                                    stack.add(Pantalla.LOGIN)
                                    onCerrarSesion?.invoke() 
                                },
                                { onSolicitarFoto?.invoke() },
                                { navegar(Pantalla.LOGROS) },
                                { navegar(Pantalla.CONFIGURACION) }
                            )
                        } else {
                            PerfilScreen(
                                sesion = sesion,
                                onVolver = { volver() },
                                onCerrarSesion = { 
                                    sesion = null
                                    stack.clear()
                                    stack.add(Pantalla.LOGIN)
                                    onCerrarSesion?.invoke() 
                                },
                                onCambiarFoto = { onSolicitarFoto?.invoke() },
                                onLogros = { navegar(Pantalla.LOGROS) },
                                onConfiguracion = { navegar(Pantalla.CONFIGURACION) },
                                fotoUrl = fotoUrl,
                                fotoContent = fotoContent ?: { modifier ->
                                    Box(modifier = modifier.background(com.example.raicesvivas.theme.Verde, CircleShape), contentAlignment = Alignment.Center) {
                                        Text(sesion?.nombreUsuario?.firstOrNull()?.uppercase() ?: "U", fontSize = 40.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = androidx.compose.ui.graphics.Color.White)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
