package com.example.raicesvivas

import android.content.Context
import com.cloudinary.android.MediaManager

object CloudinaryConfig {
    const val CLOUD_NAME = "dwj00c9i"
    const val API_KEY = "836525691336491"
    const val API_SECRET = "ZeMhT-G--TclqQjZZkO_GJ9jLaM"

    fun inicializar(context: Context) {
        val config = mapOf(
            "cloud_name" to CLOUD_NAME,
            "api_key" to API_KEY,
            "api_secret" to API_SECRET,
        )
        MediaManager.init(context, config)
    }

    fun subirFoto(
        rutaArchivo: String,
        onExito: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        MediaManager.get()
            .upload(rutaArchivo)
            .option("folder", "raicesvivas/perfiles")
            .callback(
                object : com.cloudinary.android.callback.UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = (resultData["secure_url"] as? String) ?: ""
                    onExito(url)
                }
                override fun onError(requestId: String, error: com.cloudinary.android.callback.ErrorInfo) {
                    onError(error.description)
                }
                override fun onReschedule(requestId: String, error: com.cloudinary.android.callback.ErrorInfo) {}
            })
            .dispatch()
    }
}