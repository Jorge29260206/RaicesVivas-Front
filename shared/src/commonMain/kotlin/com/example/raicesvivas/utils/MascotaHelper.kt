package com.example.raicesvivas.utils

object MascotaHelper {
    val mascotas = listOf(
        "mascota1", "mascota2", "mascota3", "mascota4", "mascota5"
    )

    fun mascotaAleatoria(): String = mascotas.random()

    fun mascotaPorPuntuacion(puntuacion: Int): String = when {
        puntuacion >= 90 -> "mascota1"
        puntuacion >= 70 -> "mascota2"
        puntuacion >= 50 -> "mascota3"
        puntuacion >= 30 -> "mascota4"
        else -> "mascota5"
    }

    fun mensajePorPuntuacion(puntuacion: Int): String = when {
        puntuacion >= 90 -> "Excelente! Xolo esta muy orgulloso de ti"
        puntuacion >= 70 -> "Muy bien! Sigue practicando con Xolo"
        puntuacion >= 50 -> "Buen intento! Xolo sabe que puedes mas"
        puntuacion >= 30 -> "No te rindas! Xolo practica contigo"
        else -> "Cada intento cuenta! Xolo te apoya siempre"
    }

    fun mensajeBienvenida(nombre: String): String {
        val mensajes = listOf(
            "Hola $nombre! Xolo esta listo para aprender",
            "Bienvenido $nombre! Las lenguas viven gracias a ti",
            "Que bueno verte $nombre! Xolo abrio el libro",
            "Lista para aprender $nombre? Xolo te acompana",
            "Hola $nombre! Tu racha sigue viva con Xolo"
        )
        return mensajes.random()
    }
}