package com.samay.app.ui.crisis

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samay.app.data.crisis.CrisisLines
import com.samay.app.ui.theme.SamayCream
import com.samay.app.ui.theme.SamayCrisis
import com.samay.app.ui.theme.SamayCrisisSoft
import com.samay.app.ui.theme.SamayForest

@Composable
fun CrisisScreen(countryCode: String?, onBack: () -> Unit) {
    val context = LocalContext.current
    val line = CrisisLines.getByCode(countryCode)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SamayCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿Necesitas ayuda profesional ahora?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SamayForest,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No estás solo. Hay personas capacitadas listas para escucharte en este momento.",
            fontSize = 16.sp,
            color = SamayForest,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SamayCrisisSoft, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = line.description,
                    color = SamayCrisis,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = line.number,
                    color = SamayCrisis,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 48.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "(${line.countryName})",
                    color = SamayCrisis,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${line.number}")
                }
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SamayCrisis),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Llamar ahora", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Esta línea de prevención es gestionada por entidades oficiales. Samay te conecta de forma gratuita. Nunca se cobrará por usar este servicio.",
            fontSize = 12.sp,
            color = SamayForest.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))
        androidx.compose.material3.TextButton(onClick = onBack) {
            Text("Volver", color = SamayForest)
        }
    }
}
