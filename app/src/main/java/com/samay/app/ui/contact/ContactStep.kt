package com.samay.app.ui.contact

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.samay.app.ui.onboarding.StepScaffold

@Composable
fun ContactStep(
    initialName: String = "",
    initialPhone: String = "",
    onBack: () -> Unit,
    onNext: (name: String, phone: String) -> Unit,
    onSkip: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }

    StepScaffold(
        title = "Persona de Confianza",
        subtitle = "En un momento de pánico, un toque enviará un mensaje o llamará a esta persona. (Opcional)",
        canAdvance = true, // they can skip by passing empty, or they can save valid data
        advanceLabel = if (name.isBlank() && phone.isBlank()) "Omitir por ahora" else "Guardar contacto",
        onBack = onBack,
        onAdvance = {
            if (name.isBlank() && phone.isBlank()) {
                onSkip()
            } else {
                onNext(name, phone)
            }
        }
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            placeholder = { Text("Ej: Mamá, Juan...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Número de teléfono") },
            placeholder = { Text("Ej: +54 9 11 1234-5678") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )
    }
}
