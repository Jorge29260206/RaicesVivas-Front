package com.example.raicesvivas.repository

import com.example.raicesvivas.ApiConfig
import com.example.raicesvivas.httpClient
import com.example.raicesvivas.models.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

class RaicesRepository {

    suspend fun registrar(nombreCompleto: String, nombreUsuario: String, correo: String, contrasena: String, edad: Int, pais: String): Resultado<String> {
        return try {
            val respuesta = httpClient.post("${ApiConfig.BASE_URL}/registro") {
                contentType(ContentType.Application.Json)
                setBody(RegistroRequestDto(nombreCompleto, nombreUsuario, correo, contrasena, edad, pais))
            }.bodyAsText()
            if (respuesta.contains("\"status\":\"ok\"")) Resultado.Exito(respuesta)
            else Resultado.Error(extraerMensaje(respuesta))
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    suspend fun login(correo: String, contrasena: String): Resultado<SesionUsuario> {
        return try {
            val respuesta = httpClient.post("${ApiConfig.BASE_URL}/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequestDto(correo, contrasena))
            }.bodyAsText()
            if (respuesta.contains("exitoso", true)) {
                val partes = respuesta.substringAfter("Bienvenido").trim().trimEnd('"', '}').split("|")
                val nombre = partes.getOrElse(0) { "" }.trim()
                val id = partes.getOrElse(1) { "0" }.trim().toIntOrNull() ?: 0
                val nombreCompleto = partes.getOrElse(2) { "" }.trim()
                Resultado.Exito(SesionUsuario(id, nombre, nombreCompleto))
            } else {
                Resultado.Error(extraerMensaje(respuesta))
            }
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    suspend fun getLenguas(): Resultado<List<LenguaDto>> {
        return try {
            val respuesta = httpClient.get("${ApiConfig.BASE_URL}/lenguas").bodyAsText()
            Resultado.Exito(json.decodeFromString(respuesta))
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    suspend fun getNiveles(lenguaId: Int): Resultado<List<NivelDto>> {
        return try {
            val respuesta = httpClient.get("${ApiConfig.BASE_URL}/lenguas/$lenguaId/niveles").bodyAsText()
            Resultado.Exito(json.decodeFromString(respuesta))
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    suspend fun getLecciones(nivelId: Int): Resultado<List<LeccionDto>> {
        return try {
            val respuesta = httpClient.get("${ApiConfig.BASE_URL}/niveles/$nivelId/lecciones").bodyAsText()
            Resultado.Exito(json.decodeFromString(respuesta))
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    suspend fun getPalabras(lenguaId: Int): Resultado<List<PalabraDto>> {
        return try {
            val respuesta = httpClient.get("${ApiConfig.BASE_URL}/lenguas/$lenguaId/palabras").bodyAsText()
            Resultado.Exito(json.decodeFromString(respuesta))
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    suspend fun getPalabrasPorLeccion(leccionId: Int): Resultado<List<PalabraDto>> {
        return try {
            val respuesta = httpClient.get("${ApiConfig.BASE_URL}/lecciones/$leccionId/palabras").bodyAsText()
            Resultado.Exito(json.decodeFromString(respuesta))
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    suspend fun getEjercicios(leccionId: Int): Resultado<List<EjercicioDto>> {
        return try {
            val respuesta = httpClient.get("${ApiConfig.BASE_URL}/lecciones/$leccionId/ejercicios").bodyAsText()
            Resultado.Exito(json.decodeFromString(respuesta))
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    suspend fun guardarProgreso(usuarioId: Int, leccionId: Int, puntuacion: Int): Resultado<String> {
        return try {
            val respuesta = httpClient.post("${ApiConfig.BASE_URL}/progreso") {
                contentType(ContentType.Application.Json)
                setBody(ProgresoRequestDto(usuarioId, leccionId, puntuacion))
            }.bodyAsText()
            Resultado.Exito(respuesta)
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

        suspend fun actualizarFoto(usuarioId: Int, fotoUrl: String): Resultado<String> {
        return try {
            val respuesta = httpClient.put("${ApiConfig.BASE_URL}/usuarios/$usuarioId/foto") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("fotoUrl" to fotoUrl))
            }.bodyAsText()
            if (respuesta.contains("\"status\":\"ok\"")) Resultado.Exito("Foto actualizada")
            else Resultado.Error(extraerMensaje(respuesta))
        } catch (e: Exception) {
            Resultado.Error("Sin conexion: ${e.message}")
        }
    }

    private fun extraerMensaje(json: String): String {
        return Regex("\"mensaje\":\"(.*?)\"").find(json)?.groupValues?.get(1) ?: "Error desconocido"
    }
}