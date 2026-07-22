package com.example.raicesvivas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.*
import com.example.raicesvivas.screens.*
import com.example.raicesvivas.theme.BeigeCalido
import com.example.raicesvivas.theme.RaicesTheme
import com.example.raicesvivas.utils.RegionHelper

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
    onLoginExitoso: ((SesionUsuario) -> Unit)? = null,
    onCerrarSesion: (() -> Unit)? = null,
    fotoUrl: String? = null,
    fotoContent: (@Composable (Modifier) -> Unit)? = null,
    perfilContent: (@Composable (SesionUsuario?, () -> Unit, () -> Unit, () -> Unit, () -> Unit, () -> Unit) -> Unit)? = null,
    mapaContent: (@Composable ((String) -> Unit, () -> Unit, String?) -> Unit)? = null
) {
    RaicesTheme {
        var pantalla by remember { mutableStateOf(if (sesionInicial != null) Pantalla.HOME else Pantalla.SPLASH) }
        var onboardingVisto by remember { mutableStateOf(sesionInicial != null) }
        var sesion by remember { mutableStateOf<SesionUsuario?>(sesionInicial) }
        var lenguaSeleccionada by remember { mutableStateOf<LenguaDto?>(null) }
        var nivelSeleccionado by remember { mutableStateOf<NivelDto?>(null) }
        var leccionSeleccionada by remember { mutableStateOf<LeccionDto?>(null) }
        var puntuacionFinal by remember { mutableStateOf(0) }

        if (sesionInicial == null) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                pantalla = if (!onboardingVisto) Pantalla.ONBOARDING else Pantalla.LOGIN
            }
        }

        Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
            Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                when (pantalla) {
                    Pantalla.SPLASH -> SplashScreen()
                    Pantalla.ONBOARDING -> OnboardingScreen {
                        onboardingVisto = true
                        pantalla = Pantalla.LOGIN
                    }
                    Pantalla.LOGIN -> LoginScreen(
                        onCrearCuenta = { pantalla = Pantalla.REGISTRO },
                        onEntrar = { pantalla = Pantalla.ENTRAR }
                    )
                    Pantalla.ENTRAR -> EntrarScreen(
                        onLoginExitoso = { s ->
                            sesion = s
                            pantalla = Pantalla.HOME
                            onLoginExitoso?.invoke(s)
                        },
                        onVolver = { pantalla = Pantalla.LOGIN }
                    )
                    Pantalla.REGISTRO -> RegistroScreen(
                        onRegistrado = { s ->
                            sesion = s
                            pantalla = Pantalla.SELECCION_LENGUA
                            onLoginExitoso?.invoke(s)
                        },
                        onVolver = { pantalla = Pantalla.LOGIN }
                    )
                    Pantalla.SELECCION_LENGUA -> SeleccionLenguaScreen(
                        onLenguaSeleccionada = { l -> lenguaSeleccionada = l; pantalla = Pantalla.HOME },
                        onVolver = { pantalla = if (sesion != null) Pantalla.HOME else Pantalla.LOGIN },
                        sugerenciaGPS = sugerenciaGPS,
                        onVerMapa = { pantalla = Pantalla.MAPA_LENGUAS }
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
                                    pantalla = Pantalla.HOME
                                },
                                { pantalla = Pantalla.SELECCION_LENGUA },
                                lenguaSeleccionada?.nombre
                            )
                        } else {
                            pantalla = Pantalla.SELECCION_LENGUA
                        }
                    }
                    Pantalla.HOME -> HomeScreen(
                        sesion = sesion,
                        lenguaActual = lenguaSeleccionada,
                        onAprender = { pantalla = Pantalla.APRENDER },
                        onDiccionario = { pantalla = Pantalla.DICCIONARIO },
                        onPerfil = { pantalla = Pantalla.PERFIL },
                        onCambiarLengua = { pantalla = Pantalla.SELECCION_LENGUA },
                        fotoContent = fotoContent ?: { modifier ->
                            Box(modifier = modifier.background(com.example.raicesvivas.theme.Verde, CircleShape), contentAlignment = Alignment.Center) {
                                Text(sesion?.nombreUsuario?.firstOrNull()?.uppercase() ?: "U", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = androidx.compose.ui.graphics.Color.White)
                            }
                        }
                    )
                    Pantalla.APRENDER -> AprenderScreen(
                        lengua = lenguaSeleccionada,
                        onNivelSeleccionado = { n -> nivelSeleccionado = n; pantalla = Pantalla.LECCIONES },
                        onVolver = { pantalla = Pantalla.HOME }
                    )
                    Pantalla.LECCIONES -> LeccionesScreen(
                        nivel = nivelSeleccionado,
                        onLeccionSeleccionada = { l -> leccionSeleccionada = l; pantalla = Pantalla.LECCION },
                        onVolver = { pantalla = Pantalla.APRENDER }
                    )
                    Pantalla.LECCION -> LeccionScreen(
                        leccion = leccionSeleccionada,
                        sesion = sesion,
                        onTerminar = { p -> puntuacionFinal = p; pantalla = Pantalla.RESULTADO },
                        onVolver = { pantalla = Pantalla.LECCIONES }
                    )
                    Pantalla.RESULTADO -> ResultadoScreen(
                        puntuacion = puntuacionFinal,
                        onContinuar = { pantalla = Pantalla.LECCIONES },
                        onHome = { pantalla = Pantalla.HOME }
                    )
                    Pantalla.DICCIONARIO -> DiccionarioScreen(
                        lengua = lenguaSeleccionada,
                        onVolver = { pantalla = Pantalla.HOME }
                    )
                    Pantalla.LOGROS -> LogrosScreen(
                        onVolver = { pantalla = Pantalla.PERFIL }
                    )
                    Pantalla.CONFIGURACION -> ConfiguracionScreen(
                        sesion = sesion,
                        onVolver = { pantalla = Pantalla.PERFIL },
                        onCerrarSesion = {
                            sesion = null
                            pantalla = Pantalla.LOGIN
                            onCerrarSesion?.invoke()
                        }
                    )
                    Pantalla.PERFIL -> {
                        if (perfilContent != null) {
                            perfilContent(
                                sesion,
                                { pantalla = Pantalla.HOME },
                                { sesion = null; pantalla = Pantalla.LOGIN; onCerrarSesion?.invoke() },
                                { onSolicitarFoto?.invoke() },
                                { pantalla = Pantalla.LOGROS },
                                { pantalla = Pantalla.CONFIGURACION }
                            )
                        } else {
                            PerfilScreen(
                                sesion = sesion,
                                onVolver = { pantalla = Pantalla.HOME },
                                onCerrarSesion = { sesion = null; pantalla = Pantalla.LOGIN; onCerrarSesion?.invoke() },
                                onCambiarFoto = { onSolicitarFoto?.invoke() },
                                onLogros = { pantalla = Pantalla.LOGROS },
                                onConfiguracion = { pantalla = Pantalla.CONFIGURACION },
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
