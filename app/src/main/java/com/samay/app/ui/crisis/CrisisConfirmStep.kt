package com.samay.app.ui.crisis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samay.app.data.crisis.CrisisLines
import com.samay.app.ui.onboarding.OptionRow
import com.samay.app.ui.onboarding.StepScaffold
import com.samay.app.ui.theme.SamayCrisis

@Composable
fun CrisisConfirmStep(
    selectedCountry: String?,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    StepScaffold(
        title = "Línea de Crisis",
        subtitle = "Selecciona tu país para configurar la línea de prevención del suicidio de acceso rápido. Siempre será gratis.",
        canAdvance = selectedCountry != null,
        advanceLabel = "Guardar y finalizar",
        onBack = onBack,
        onAdvance = onNext
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))

            CrisisLines.lines.forEach { line ->
                OptionRow(
                    text = "${line.countryName} - ${line.number}",
                    selected = selectedCountry == line.countryCode
                ) {
                    onSelect(line.countryCode)
                }
            }

            if (selectedCountry != null) {
                Spacer(modifier = Modifier.height(16.dp))
                val line = CrisisLines.getByCode(selectedCountry)
                Text(
                    text = "Línea seleccionada: ${line.description} (${line.number})",
                    color = SamayCrisis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
