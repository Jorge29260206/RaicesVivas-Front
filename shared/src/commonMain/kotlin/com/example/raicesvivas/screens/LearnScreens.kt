package com.example.raicesvivas.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raicesvivas.models.*
import com.example.raicesvivas.repository.RaicesRepository
import com.example.raicesvivas.theme.*
import kotlinx.coroutines.launch

@Composable
fun AprenderScreen(lengua: LenguaDto?, onNivelSeleccionado: (NivelDto) -> Unit, onVolver: () -> Unit) {
    val repo = remember { RaicesRepository() }
    var niveles by remember { mutableStateOf<List<NivelDto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(lengua?.id) {
        lengua?.let {
            when (val r = repo.getNiveles(it.id)) {
                is Resultado.Exito -> niveles = r.datos
                is Resultado.Error -> error = r.mensaje
                Resultado.Cargando -> {}
            }
        }
        cargando = false
    }

    BackHandler { onVolver() }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                TopBarConRegreso("Aprender ${lengua?.nombre ?: ""}", onVolver)
                Text("Elige tu nivel para comenzar", fontSize = 14.sp, color = CafeTierra.copy(alpha = 0.7f))
                Spacer(Modifier.height(16.dp))
                if (cargando) Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Verde) }
                error?.let { Text(it, color = Terracota, fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
            }
            items(niveles) { nivel ->
                val colores = listOf(Verde, Turquesa, Terracota)
                val emojis = listOf("ðŸŒ±", "ðŸŒ¿", "ðŸŒ³")
                val color = colores.getOrElse(nivel.orden - 1) { Verde }
                val emoji = emojis.getOrElse(nivel.orden - 1) { "ðŸŒ¿" }
                Card(onClick = { onNivelSeleccionado(nivel) }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = color)) {
                    Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(emoji, fontSize = 40.sp)
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Nivel ${nivel.orden}", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(nivel.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            nivel.descripcion?.let { Text(it, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f)) }
                        }
                        Text("â†’", fontSize = 24.sp, color = Color.White)
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun LeccionesScreen(nivel: NivelDto?, onLeccionSeleccionada: (LeccionDto) -> Unit, onVolver: () -> Unit) {
    val repo = remember { RaicesRepository() }
    var lecciones by remember { mutableStateOf<List<LeccionDto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(nivel?.id) {
        nivel?.let {
            when (val r = repo.getLecciones(it.id)) {
                is Resultado.Exito -> lecciones = r.datos
                is Resultado.Error -> error = r.mensaje
                Resultado.Cargando -> {}
            }
        }
        cargando = false
    }

    BackHandler { onVolver() }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                TopBarConRegreso(nivel?.nombre ?: "Lecciones", onVolver)
                Text("Elige una leccion para comenzar", fontSize = 14.sp, color = CafeTierra.copy(alpha = 0.7f))
                Spacer(Modifier.height(16.dp))
                if (cargando) Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Verde) }
                error?.let { Text(it, color = Terracota, fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
            }
            items(lecciones) { leccion ->
                Card(onClick = { onLeccionSeleccionada(leccion) }, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(48.dp).background(Verde.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
                            Text("${leccion.orden}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Verde)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(leccion.titulo, fontWeight = FontWeight.Bold, color = CafeTierra, fontSize = 16.sp)
                            leccion.descripcion?.let { Text(it, color = GrisSuave, fontSize = 13.sp) }
                        }
                        Text("â–¶", color = Verde, fontSize = 20.sp)
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun LeccionScreen(leccion: LeccionDto?, sesion: SesionUsuario?, onTerminar: (Int) -> Unit, onVolver: () -> Unit) {
    val repo = remember { RaicesRepository() }
    val scope = rememberCoroutineScope()
    var palabras by remember { mutableStateOf<List<PalabraDto>>(emptyList()) }
    var indiceActual by remember { mutableStateOf(0) }
    var respuestaSeleccionada by remember { mutableStateOf<String?>(null) }
    var mostrarResultado by remember { mutableStateOf(false) }
    var aciertos by remember { mutableStateOf(0) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(leccion?.id) {
        leccion?.let {
            when (val r = repo.getPalabrasPorLeccion(it.id)) {
                is Resultado.Exito -> palabras = r.datos
                is Resultado.Error -> error = r.mensaje
                Resultado.Cargando -> {}
            }
        }
        cargando = false
    }

    BackHandler { onVolver() }

    if (cargando) { Box(Modifier.fillMaxSize().background(BeigeCalido), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Verde) }; return }

    if (error != null) {
        Box(Modifier.fillMaxSize().background(BeigeCalido), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                Text("Sin conexion", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
                Spacer(Modifier.height(8.dp))
                Text(error ?: "", fontSize = 14.sp, color = Terracota, textAlign = TextAlign.Center)
                Spacer(Modifier.height(24.dp))
                Button(onClick = onVolver, colors = ButtonDefaults.buttonColors(containerColor = Verde)) { Text("Volver", color = Color.White) }
            }
        }
        return
    }

    if (palabras.isEmpty()) {
        Box(Modifier.fillMaxSize().background(BeigeCalido), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Sin ejercicios disponibles", color = CafeTierra, fontSize = 16.sp)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onVolver, colors = ButtonDefaults.buttonColors(containerColor = Verde)) { Text("Volver", color = Color.White) }
            }
        }
        return
    }

    val palabraActual = palabras.getOrNull(indiceActual)
    val progreso = indiceActual.toFloat() / palabras.size
    val opcionesRespuesta = remember(indiceActual, palabras) {
        val correcta = palabraActual?.traduccion ?: ""
        val otras = palabras.filter { it.id != palabraActual?.id }.map { it.traduccion }.shuffled().take(3)
        (otras + correcta).shuffled()
    }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onVolver) { Text("âœ•", color = GrisSuave, fontSize = 20.sp) }
                Spacer(Modifier.width(8.dp))
                LinearProgressIndicator(progress = { progreso }, modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)), color = Verde, trackColor = GrisSuave.copy(alpha = 0.3f))
                Spacer(Modifier.width(8.dp))
                Text("${indiceActual + 1}/${palabras.size}", fontSize = 13.sp, color = GrisSuave)
            }
            Spacer(Modifier.height(32.dp))
            Text("Â¿Que significa?", fontSize = 16.sp, color = GrisSuave)
            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Verde.copy(alpha = 0.1f))) {
                Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(palabraActual?.palabraOriginal ?: "", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = CafeTierra, textAlign = TextAlign.Center)
                    palabraActual?.pronunciacion?.let { Spacer(Modifier.height(8.dp)); Text("[$it]", fontSize = 16.sp, color = GrisSuave, textAlign = TextAlign.Center) }
                }
            }
            Spacer(Modifier.height(32.dp))
            Text("Elige la respuesta correcta", fontSize = 14.sp, color = GrisSuave)
            Spacer(Modifier.height(12.dp))
            opcionesRespuesta.forEach { opcion ->
                val esSeleccionada = respuestaSeleccionada == opcion
                val esCorrecta = opcion == palabraActual?.traduccion
                val colorFondo = when { !mostrarResultado && esSeleccionada -> Verde.copy(alpha = 0.2f); mostrarResultado && esCorrecta -> Verde.copy(alpha = 0.2f); mostrarResultado && esSeleccionada && !esCorrecta -> Terracota.copy(alpha = 0.2f); else -> Color.White }
                val colorBorde = when { !mostrarResultado && esSeleccionada -> Verde; mostrarResultado && esCorrecta -> Verde; mostrarResultado && esSeleccionada && !esCorrecta -> Terracota; else -> GrisSuave.copy(alpha = 0.3f) }
                Card(onClick = { if (!mostrarResultado) { respuestaSeleccionada = opcion; mostrarResultado = true; if (opcion == palabraActual?.traduccion) aciertos++ } }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(2.dp, colorBorde, RoundedCornerShape(12.dp)), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = colorFondo)) {
                    Text(opcion, modifier = Modifier.padding(16.dp), fontSize = 16.sp, color = CafeTierra, fontWeight = if (esSeleccionada) FontWeight.Bold else FontWeight.Normal)
                }
            }
            Spacer(Modifier.weight(1f))
            if (mostrarResultado) {
                val esCorrecta = respuestaSeleccionada == palabraActual?.traduccion
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = if (esCorrecta) Verde.copy(alpha = 0.1f) else Terracota.copy(alpha = 0.1f))) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(if (esCorrecta) "âœ“" else "âœ—", fontSize = 24.sp, color = if (esCorrecta) Verde else Terracota, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(if (esCorrecta) "Correcto!" else "Incorrecto", fontWeight = FontWeight.Bold, color = if (esCorrecta) Verde else Terracota)
                            if (!esCorrecta) Text("Respuesta: ${palabraActual?.traduccion}", fontSize = 13.sp, color = CafeTierra)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (indiceActual < palabras.size - 1) { indiceActual++; respuestaSeleccionada = null; mostrarResultado = false }
                        else {
                            val puntuacion = (aciertos.toFloat() / palabras.size * 100).toInt()
                            sesion?.let { s -> leccion?.let { l -> scope.launch { repo.guardarProgreso(s.id, l.id, puntuacion) } } }
                            onTerminar(puntuacion)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Verde),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(if (indiceActual < palabras.size - 1) "Siguiente" else "Ver resultado", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ResultadoScreen(puntuacion: Int, onContinuar: () -> Unit, onHome: () -> Unit) {
    val emoji = when { puntuacion >= 90 -> "ðŸ†"; puntuacion >= 70 -> "â­"; puntuacion >= 50 -> "ðŸ‘"; else -> "ðŸ’ª" }
    val mensaje = when { puntuacion >= 90 -> "Excelente!"; puntuacion >= 70 -> "Muy bien!"; puntuacion >= 50 -> "Buen intento!"; else -> "Sigue practicando!" }
    val color = if (puntuacion >= 70) Verde else Terracota

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text(emoji, fontSize = 80.sp)
            Spacer(Modifier.height(16.dp))
            Text(mensaje, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CafeTierra)
            Spacer(Modifier.height(8.dp))
            Text("Tu puntuacion", fontSize = 16.sp, color = GrisSuave)
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.size(120.dp).background(color.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Text("$puntuacion%", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = color)
            }
            Spacer(Modifier.height(48.dp))
            Button(onClick = onContinuar, colors = ButtonDefaults.buttonColors(containerColor = Verde), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Continuar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onHome, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = CafeTierra)) {
                Text("Ir al inicio", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun DiccionarioScreen(lengua: LenguaDto?, onVolver: () -> Unit) {
    val repo = remember { RaicesRepository() }
    var palabras by remember { mutableStateOf<List<PalabraDto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var busqueda by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(lengua?.id) {
        lengua?.let {
            when (val r = repo.getPalabras(it.id)) {
                is Resultado.Exito -> palabras = r.datos
                is Resultado.Error -> error = r.mensaje
                Resultado.Cargando -> {}
            }
        }
        cargando = false
    }

    BackHandler { onVolver() }

    val palabrasFiltradas = palabras.filter { busqueda.isBlank() || it.palabraOriginal.contains(busqueda, true) || it.traduccion.contains(busqueda, true) }

    Box(modifier = Modifier.fillMaxSize().background(BeigeCalido)) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            item {
                TopBarConRegreso("Diccionario ${lengua?.nombre ?: ""}", onVolver)
                OutlinedTextField(value = busqueda, onValueChange = { busqueda = it }, label = { Text("Buscar palabra...") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
                Spacer(Modifier.height(16.dp))
                if (cargando) Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Verde) }
                error?.let { Text(it, color = Terracota, fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
                if (!cargando && palabrasFiltradas.isEmpty() && error == null) Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { Text("No se encontraron palabras", color = GrisSuave, fontSize = 14.sp) }
            }
            items(palabrasFiltradas) { palabra ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(palabra.palabraOriginal, fontWeight = FontWeight.Bold, color = CafeTierra, fontSize = 16.sp)
                            palabra.pronunciacion?.let { Text("[$it]", color = GrisSuave, fontSize = 12.sp) }
                            Text(palabra.traduccion, color = Turquesa, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            palabra.ejemploUso?.let { Text(it, color = CafeTierra.copy(alpha = 0.6f), fontSize = 12.sp) }
                        }
                        Box(Modifier.size(40.dp).background(if (palabra.audioUrl != null) Verde.copy(alpha = 0.1f) else GrisSuave.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                            Text(if (palabra.audioUrl != null) "ðŸ”Š" else "ðŸ“", fontSize = 18.sp)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}