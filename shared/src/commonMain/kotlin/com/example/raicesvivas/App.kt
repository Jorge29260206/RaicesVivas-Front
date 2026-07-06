package com.example.raicesvivas

import androidx.compose.runtime.*
import com.example.raicesvivas.models.*
import com.example.raicesvivas.screens.*
import com.example.raicesvivas.theme.RaicesTheme

enum class Pantalla {
    SPLASH, ONBOARDING, LOGIN, ENTRAR, REGISTRO,
    SELECCION_LENGUA, HOME, APRENDER, LECCIONES,
    LECCION, RESULTADO, DICCIONARIO, PERFIL
}

@Composable
fun App(
    sesionInicial: SesionUsuario? = null,
    onSolicitarFoto: (() -> Unit)? = null,
    onLoginExitoso: ((SesionUsuario) -> Unit)? = null,
    onCerrarSesion: (() -> Unit)? = null
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
                onVolver = { pantalla = if (sesion != null) Pantalla.HOME else Pantalla.LOGIN }
            )
            Pantalla.HOME -> HomeScreen(
                sesion = sesion,
                lenguaActual = lenguaSeleccionada,
                onAprender = { pantalla = Pantalla.APRENDER },
                onDiccionario = { pantalla = Pantalla.DICCIONARIO },
                onPerfil = { pantalla = Pantalla.PERFIL },
                onCambiarLengua = { pantalla = Pantalla.SELECCION_LENGUA }
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
            Pantalla.PERFIL -> PerfilScreen(
                sesion = sesion,
                onVolver = { pantalla = Pantalla.HOME },
                onCerrarSesion = {
                    sesion = null
                    pantalla = Pantalla.LOGIN
                    onCerrarSesion?.invoke()
                },
                onCambiarFoto = { onSolicitarFoto?.invoke() }
            )
        }
    }
}