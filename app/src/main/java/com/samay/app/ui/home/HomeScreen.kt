package com.samay.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samay.app.ui.theme.SamayCream
import com.samay.app.ui.theme.SamayCrisisSoft
import com.samay.app.ui.theme.SamayCrisis
import com.samay.app.ui.theme.SamayForest
import com.samay.app.ui.theme.SamayForestSoft
import com.samay.app.ui.theme.SamaySage

@Composable
fun HomeScreen(
    onTherapyClick: () -> Unit,
    onCrisisClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPremiumClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SamayCream)
            .padding(24.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Samay",
                color = SamayForest,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                IconButton(onClick = onPremiumClick) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Premium", tint = SamayForest)
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Ajustes", tint = SamayForest)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // CTA Gigante: Modo Terapia
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Círculo perfecto
                .clip(CircleShape)
                .background(SamaySage)
                .clickable { onTherapyClick() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(SamayForest),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Modo\nTerapia",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Card Línea de Crisis
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable { onCrisisClick() },
            colors = CardDefaults.cardColors(containerColor = SamayCrisisSoft),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Línea de Crisis",
                        color = SamayCrisis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Gratis. Confidencial. 24/7.",
                        color = SamayCrisis.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
                // Placeholder para un ícono de teléfono si lo tuvieran
                Text("📞", fontSize = 24.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
