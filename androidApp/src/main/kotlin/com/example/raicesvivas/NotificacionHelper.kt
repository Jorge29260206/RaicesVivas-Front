package com.example.raicesvivas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

object NotificacionHelper {

    private const val CANAL_ID = "raicesvivas_racha"
    private const val CANAL_NOMBRE = "Racha diaria RaicesVivas"

    // =====================================================
    // CONFIGURACION DE NOTIFICACIONES
    // Cambia estos valores para ajustar el comportamiento
    // =====================================================

    // Cada cuantas horas se manda el recordatorio (default: 24 = una vez al dia)
    const val INTERVALO_HORAS: Long = 24

    // Cuantas horas despues de instalar manda la primera notificacion (default: 20)
    const val DELAY_INICIAL_HORAS: Long = 20

    // =====================================================

    private val MENSAJES_RACHA = listOf(
        "Xolo te extraña! No pierdas tu racha de aprendizaje 🐕",
        "Las lenguas indigenas necesitan tu voz hoy! Practica con Xolo",
        "Tu racha esta en peligro! Xolo ya abrio el libro, faltas tu",
        "Un dia sin practicar es un dia que Xolo espera triste",
        "Las palabras de tus ancestros te esperan! Entra y practica",
        "Duolingo no tiene Nahuatl, pero RaicesVivas si! Aprovechalo",
        "Xolo dice: cada palabra que aprendes preserva una cultura",
        "Tu racha no se rompe sola, pero si no practicas... 👀",
        "Las lenguas indigenas viven gracias a ti! No las dejes morir",
        "5 minutos con Xolo son suficientes para mantener tu racha",
    )

    private val MENSAJES_BIENVENIDA = listOf(
        "Bienvenido! Xolo esta listo para aprender contigo 🐕📚",
        "Hola! Las lenguas indigenas te necesitan hoy",
        "Que bueno que estas aqui! Xolo ya tenia el libro abierto",
        "Tu regresaste! Xolo estaba esperandote con palabras nuevas",
        "Listo para aprender? Xolo tiene mucho que ensenarte hoy"
    )

    fun crearCanal(context: Context) {
        val canal = NotificationChannel(
            CANAL_ID,
            CANAL_NOMBRE,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Recordatorios diarios para mantener tu racha de aprendizaje"
            enableVibration(true)
        }
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(canal)
    }

    fun mostrarNotificacionBienvenida(context: Context, nombre: String) {
        crearCanal(context)
        val mensaje = "Hola $nombre! " + MENSAJES_BIENVENIDA.random()
        val notif = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("RaicesVivas te da la bienvenida!")
            .setContentText(mensaje)
            .setStyle(NotificationCompat.BigTextStyle().bigText(mensaje))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        context.getSystemService(NotificationManager::class.java).notify(1001, notif)
    }

    fun programarRecordatorioDiario(context: Context) {
        crearCanal(context)
        val trabajo = PeriodicWorkRequestBuilder<RachaWorker>(
            INTERVALO_HORAS, TimeUnit.HOURS
        )
            .setInitialDelay(DELAY_INICIAL_HORAS, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "racha_diaria",
            ExistingPeriodicWorkPolicy.KEEP,
            trabajo
        )
    }

    fun cancelarRecordatorio(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("racha_diaria")
    }

    fun obtenerMensajeRacha(): String = MENSAJES_RACHA.random()
}

class RachaWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val mensaje = NotificacionHelper.obtenerMensajeRacha()
        val notif = NotificationCompat.Builder(applicationContext, "raicesvivas_racha")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Es hora de practicar con Xolo!")
            .setContentText(mensaje)
            .setStyle(NotificationCompat.BigTextStyle().bigText(mensaje))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        applicationContext.getSystemService(NotificationManager::class.java)
            .notify(1002, notif)
        return Result.success()
    }
}