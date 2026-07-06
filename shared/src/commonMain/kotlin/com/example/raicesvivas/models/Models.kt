package com.example.raicesvivas.models

import kotlinx.serialization.Serializable

data class SesionUsuario(val id: Int, val nombreUsuario: String, val nombreCompleto: String, val fotoUrl: String? = null)

@Serializable data class RegistroRequestDto(val nombreCompleto: String, val nombreUsuario: String, val correo: String, val contrasena: String, val edad: Int, val pais: String)
@Serializable data class LoginRequestDto(val correo: String, val contrasena: String)
@Serializable data class LenguaDto(val id: Int, val nombre: String, val region: String, val descripcion: String? = null, val imagenUrl: String? = null, val activa: Boolean = true)
@Serializable data class NivelDto(val id: Int, val lenguaId: Int, val nombre: String, val descripcion: String? = null, val orden: Int)
@Serializable data class LeccionDto(val id: Int, val nivelId: Int, val titulo: String, val descripcion: String? = null, val orden: Int)
@Serializable data class PalabraDto(val id: Int, val lenguaId: Int, val leccionId: Int? = null, val palabraOriginal: String, val traduccion: String, val pronunciacion: String? = null, val imagenUrl: String? = null, val audioUrl: String? = null, val ejemploUso: String? = null, val nivelDificultad: Int = 1)
@Serializable data class OpcionDto(val id: Int, val texto: String, val esCorrecta: Boolean, val audioUrl: String? = null)
@Serializable data class EjercicioDto(val id: Int, val leccionId: Int, val tipo: String, val pregunta: String, val audioPreguntaUrl: String? = null, val imagenUrl: String? = null, val orden: Int, val opciones: List<OpcionDto>)
@Serializable data class ProgresoRequestDto(val usuarioId: Int, val leccionId: Int, val puntuacion: Int)