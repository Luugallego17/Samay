package com.samay.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star

import com.samay.app.ui.theme.SamayMuted
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
                                            androidx.compose.material3.Icon(
                            painter = androidx.compose.ui.res.painterResource(id = com.samay.app.R.drawable.ic_launcher_foreground),
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.size(72.dp)
                        )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Ajustes", tint = SamayForest)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // CTA Gigante: Modo Terapia
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¿Necesitás un momento?",
                color = SamayForest,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f) // Círculo perfecto
                    .clip(CircleShape)
                    .background(SamaySage.copy(alpha = 0.5f))
                    .clickable { onTherapyClick() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(SamayForest),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        androidx.compose.material3.Icon(
                            painter = androidx.compose.ui.res.painterResource(id = com.samay.app.R.drawable.ic_launcher_foreground),
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.size(72.dp)
                        )
                        Text(
                            text = "Modo Terapia",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Respiración guiada + tu kit de calma",
                color = SamayMuted,
                fontSize = 14.sp
            )
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
