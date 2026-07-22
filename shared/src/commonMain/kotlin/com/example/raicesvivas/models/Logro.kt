package com.example.raicesvivas.models

data class Logro(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val emoji: String,
    val desbloqueado: Boolean = false
)

object LogrosData {
    fun listaCompleta(palabrasAprendidas: Int, audiosGrabados: Int, rachaDias: Int, categoriasCompletadas: Int): List<Logro> {
        return listOf(
            Logro("primera_palabra", "Primera palabra", "Aprendiste tu primera palabra.", "🌱", palabrasAprendidas >= 1),
            Logro("voz_en_accion", "Voz en acción", "Grabaste tu primera palabra.", "🎙️", audiosGrabados >= 1),
            Logro("aprendiz_constante", "Aprendiz constante", "Aprendiste 10 palabras.", "📚", palabrasAprendidas >= 10),
            Logro("racha_7", "Racha de 7 días", "Aprendiste durante 7 días seguidos.", "🔥", rachaDias >= 7),
            Logro("explorador", "Explorador", "Completaste una categoría.", "🧭", categoriasCompletadas >= 1),
            Logro("comunidad_viva", "Comunidad viva", "Compartiste contenido con tu comunidad.", "🤝", false),
            Logro("maestro_comunitario", "Maestro comunitario", "Aprende 100 palabras.", "🎓", palabrasAprendidas >= 100),
            Logro("sabiduria_ancestral", "Sabiduría ancestral", "Graba 20 palabras o frases.", "✨", audiosGrabados >= 20),
            Logro("leyenda_viva", "Leyenda viva", "Completa todas las categorías.", "👑", categoriasCompletadas >= 6)
        )
    }
}
