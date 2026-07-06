package com.example.raicesvivas

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.raicesvivas.models.SesionUsuario
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion_raicesvivas")

object SesionDataStore {
    private val KEY_ID = intPreferencesKey("usuario_id")
    private val KEY_NOMBRE = stringPreferencesKey("usuario_nombre")
    private val KEY_NOMBRE_COMPLETO = stringPreferencesKey("usuario_nombre_completo")
    private val KEY_FOTO = stringPreferencesKey("usuario_foto")
    private val KEY_LENGUA_ID = intPreferencesKey("lengua_id")
    private val KEY_LENGUA_NOMBRE = stringPreferencesKey("lengua_nombre")
    private val KEY_LENGUA_REGION = stringPreferencesKey("lengua_region")

    suspend fun guardarSesion(context: Context, sesion: SesionUsuario) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ID] = sesion.id
            prefs[KEY_NOMBRE] = sesion.nombreUsuario
            prefs[KEY_NOMBRE_COMPLETO] = sesion.nombreCompleto
            sesion.fotoUrl?.let { prefs[KEY_FOTO] = it }
        }
    }

    suspend fun obtenerSesion(context: Context): SesionUsuario? {
        val prefs = context.dataStore.data.first()
        val id = prefs[KEY_ID] ?: return null
        val nombre = prefs[KEY_NOMBRE] ?: return null
        val nombreCompleto = prefs[KEY_NOMBRE_COMPLETO] ?: ""
        val foto = prefs[KEY_FOTO]
        return SesionUsuario(id, nombre, nombreCompleto, foto)
    }

    suspend fun cerrarSesion(context: Context) {
        context.dataStore.edit { it.clear() }
    }

    suspend fun guardarLengua(context: Context, id: Int, nombre: String, region: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LENGUA_ID] = id
            prefs[KEY_LENGUA_NOMBRE] = nombre
            prefs[KEY_LENGUA_REGION] = region
        }
    }

    suspend fun obtenerLenguaId(context: Context): Int? {
        return context.dataStore.data.first()[KEY_LENGUA_ID]
    }
}