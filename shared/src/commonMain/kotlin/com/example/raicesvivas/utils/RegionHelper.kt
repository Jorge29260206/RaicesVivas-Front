package com.example.raicesvivas.utils

object RegionHelper {

    data class Region(
        val estado: String,
        val latMin: Double, val latMax: Double,
        val lonMin: Double, val lonMax: Double,
        val lengua: String
    )

    data class SugerenciaGPS(
        val estado: String,
        val lenguaSugerida: String,
        val mensaje: String,
        val tienesSugerencia: Boolean
    )

    private val regionesMexico = listOf(
        Region("Yucatan",    18.5, 21.5, -91.0, -86.5, "Maya"),
        Region("Campeche",   17.5, 20.5, -92.5, -89.5, "Maya"),
        Region("Quintana Roo", 17.8, 21.6, -88.0, -86.5, "Maya"),
        Region("Oaxaca",     15.6, 18.5, -98.5, -93.5, "Zapoteco"),
        Region("Michoacan",  18.8, 20.4, -102.5, -100.0, "Purepecha"),
        Region("Queretaro",  20.0, 21.7, -100.5, -99.0, "Otomi"),
        Region("Hidalgo",    19.5, 21.4, -99.5, -97.8, "Nahuatl"),
        Region("Puebla",     17.8, 20.8, -99.0, -96.7, "Nahuatl"),
        Region("Veracruz",   17.1, 22.5, -98.5, -93.5, "Nahuatl"),
        Region("CDMX",       19.0, 19.6, -99.4, -98.9, "Nahuatl"),
        Region("Guerrero",   16.3, 18.8, -102.2, -98.0, "Mixteco"),
        Region("Chiapas",    14.5, 17.9, -94.0, -90.3, "Maya"),
    )

    fun sugerirLengua(latitud: Double, longitud: Double): SugerenciaGPS {
        val region = regionesMexico.find { r ->
            latitud in r.latMin..r.latMax &&
            longitud in r.lonMin..r.lonMax
        }
        return if (region != null) {
            SugerenciaGPS(
                estado = region.estado,
                lenguaSugerida = region.lengua,
                mensaje = "Segun tu ubicacion en , te sugerimos aprender ",
                tienesSugerencia = true
            )
        } else {
            SugerenciaGPS(
                estado = "Mexico",
                lenguaSugerida = "Nahuatl",
                mensaje = "Elige la lengua que quieras aprender",
                tienesSugerencia = false
            )
        }
    }
}