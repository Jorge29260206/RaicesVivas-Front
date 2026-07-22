package com.example.raicesvivas

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class GeolocalizacionManager(private val context: Context) {

    private val fusedClient = LocationServices
        .getFusedLocationProviderClient(context)

    fun obtenerUbicacion(
        onUbicacion: (latitud: Double, longitud: Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val permisoFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permisoCoarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permisoFine != PackageManager.PERMISSION_GRANTED &&
            permisoCoarse != PackageManager.PERMISSION_GRANTED) {
            onError("Permiso de ubicacion no concedido")
            return
        }

        fusedClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onUbicacion(location.latitude, location.longitude)
                } else {
                    solicitarUbicacionNueva(onUbicacion, onError)
                }
            }
            .addOnFailureListener {
                onError("Error al obtener ubicacion: ")
            }
    }

    private fun solicitarUbicacionNueva(
        onUbicacion: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setDurationMillis(10000)
            .build()

        val permisoFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permisoFine != PackageManager.PERMISSION_GRANTED) {
            onError("Permiso requerido")
            return
        }

        fusedClient.getCurrentLocation(request, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    onUbicacion(location.latitude, location.longitude)
                } else {
                    onError("No se pudo obtener la ubicacion")
                }
            }
            .addOnFailureListener {
                onError("Error: ")
            }
    }
}