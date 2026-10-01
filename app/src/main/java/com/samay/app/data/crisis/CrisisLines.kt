package com.samay.app.data.crisis

data class CrisisLine(val countryCode: String, val countryName: String, val number: String, val description: String)

object CrisisLines {
    val lines = listOf(
        CrisisLine("AR", "Argentina", "135", "Línea de Asistencia al Suicida (CAPS)"),
        CrisisLine("BO", "Bolivia", "161", "Teléfono de la Esperanza"),
        CrisisLine("CL", "Chile", "*4141", "Línea Prevención del Suicidio"),
        CrisisLine("CO", "Colombia", "106", "Línea de Ayuda"),
        CrisisLine("MX", "México", "800 911 2000", "Línea de la Vida"),
        CrisisLine("PE", "Perú", "113", "Línea de Ayuda (Opción 5)"),
        CrisisLine("US", "Estados Unidos", "988", "Suicide & Crisis Lifeline"),
        CrisisLine("OTHER", "Otro / Internacional", "911", "Emergencias Generales")
    )

    fun getByCode(code: String?): CrisisLine {
        return lines.find { it.countryCode == code } ?: lines.last()
    }
}
