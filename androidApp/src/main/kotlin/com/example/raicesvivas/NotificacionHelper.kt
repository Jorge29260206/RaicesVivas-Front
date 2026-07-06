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

    fun crearCanal(context: Context) {
        val canal = NotificationChannel(
            CANAL_ID,
            CANAL_NOMBRE,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Recordatorio diario para mantener tu racha de aprendizaje"
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(canal)
    }

    fun mostrarNotificacionBienvenida(context: Context, nombre: String) {
        crearCanal(context)
        val mensajes = listOf(
            "Bienvenido $nombre! Xolo te esperaba para aprender juntos",
            "Hola $nombre! Es hora de practicar tu lengua indigena",
            "Que bueno que estas aqui $nombre! Xolo tiene palabras nuevas para ti",
            "Bienvenido de nuevo $nombre! Tu racha sigue viva",
            "Listo para aprender $nombre? Xolo ya abrio el libro"
        )
        val mensaje = mensajes.random()
        val notif = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("RaicesVivas")
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(1001, notif)
    }

    fun programarRecordatorioDiario(context: Context) {
        crearCanal(context)
        val trabajo = PeriodicWorkRequestBuilder<RachaWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(20, TimeUnit.HOURS)
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
}

class RachaWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val mensajes = listOf(
            "Xolo te extraña! No pierdas tu racha de aprendizaje",
            "Tu lengua indigena te necesita! Practica hoy con Xolo",
            "Llevas dias aprendiendo, no lo dejes ir! Xolo te espera",
            "Un momento con Xolo es suficiente para mantener tu racha",
            "Las lenguas indigenas viven gracias a ti! Practica hoy"
        )
        val notif = NotificationCompat.Builder(applicationContext, "raicesvivas_racha")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Xolo te extraña!")
            .setContentText(mensajes.random())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        val manager = applicationContext.getSystemService(NotificationManager::class.java)
        manager.notify(1002, notif)
        return Result.success()
    }
}