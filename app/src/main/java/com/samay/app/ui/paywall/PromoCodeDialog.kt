package com.samay.app.ui.paywall

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samay.app.billing.PremiumRepository
import com.samay.app.ui.theme.SamayForest

@Composable
fun PromoCodeDialog(
    onDismiss: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Código Promocional") },
        text = {
            Column {
                if (success) {
                    Text("¡Código aceptado! Ya eres Premium.", color = SamayForest)
                } else {
                    Text("Ingresa tu código de acceso:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it; error = null },
                        isError = error != null,
                        singleLine = true
                    )
                    if (error != null) {
                        Text(error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        },
        confirmButton = {
            if (success) {
                TextButton(onClick = onDismiss) { Text("Cerrar") }
            } else {
                TextButton(
                    onClick = {
                        val validCodes = listOf("SHIPATON2026", "NEXTGEN", "PEACEPRIZE", "CALM")
                        if (code.trim().uppercase() in validCodes) {
                            PremiumRepository.simulatePurchaseSuccess()
                            success = true
                        } else {
                            error = "Código inválido"
                        }
                    }
                ) {
                    Text("Canjear")
                }
            }
        },
        dismissButton = {
            if (!success) {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
            }
        }
    )
}
