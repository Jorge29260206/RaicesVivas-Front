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

    suspend fun registrar(nombreCompleto: String, nombreUsuario: String, correo: String, contrasena: String, edad: Int, pais: String): String {
        return httpClient.post("${ApiConfig.BASE_URL}/registro") {
            contentType(ContentType.Application.Json)
            setBody(RegistroRequestDto(nombreCompleto, nombreUsuario, correo, contrasena, edad, pais))
        }.bodyAsText()
    }

    suspend fun login(correo: String, contrasena: String): String {
        return httpClient.post("${ApiConfig.BASE_URL}/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(correo, contrasena))
        }.bodyAsText()
    }

    suspend fun getLenguas(): List<LenguaDto> {
        val response = httpClient.get("${ApiConfig.BASE_URL}/lenguas").bodyAsText()
        return json.decodeFromString(response)
    }

    suspend fun getNiveles(lenguaId: Int): List<NivelDto> {
        val response = httpClient.get("${ApiConfig.BASE_URL}/lenguas/$lenguaId/niveles").bodyAsText()
        return json.decodeFromString(response)
    }

    suspend fun getLecciones(nivelId: Int): List<LeccionDto> {
        val response = httpClient.get("${ApiConfig.BASE_URL}/niveles/$nivelId/lecciones").bodyAsText()
        return json.decodeFromString(response)
    }

    suspend fun getPalabras(lenguaId: Int): List<PalabraDto> {
        val response = httpClient.get("${ApiConfig.BASE_URL}/lenguas/$lenguaId/palabras").bodyAsText()
        return json.decodeFromString(response)
    }

    suspend fun getPalabrasPorLeccion(leccionId: Int): List<PalabraDto> {
        val response = httpClient.get("${ApiConfig.BASE_URL}/lecciones/$leccionId/palabras").bodyAsText()
        return json.decodeFromString(response)
    }

    suspend fun getEjercicios(leccionId: Int): List<EjercicioDto> {
        val response = httpClient.get("${ApiConfig.BASE_URL}/lecciones/$leccionId/ejercicios").bodyAsText()
        return json.decodeFromString(response)
    }

    suspend fun guardarProgreso(usuarioId: Int, leccionId: Int, puntuacion: Int): String {
        return httpClient.post("${ApiConfig.BASE_URL}/progreso") {
            contentType(ContentType.Application.Json)
            setBody(ProgresoRequestDto(usuarioId, leccionId, puntuacion))
        }.bodyAsText()
    }
}