package com.samay.app.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*
 * Componentes del design system (B2 #13).
 * Los nombres coinciden con el handoff de Figma; ver docs/ux/DESIGN_SYSTEM.md.
 * Todos son "stateless": reciben datos y callbacks, no leen repositorios.
 */

/** Acción principal de la pantalla. Una sola por pantalla. */
@Composable
fun SamayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = SamayRadius.md,
        colors = ButtonDefaults.buttonColors(
            containerColor = SamayForest,
            contentColor = SamayOnDark
        )
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

/** Acción secundaria: borde forest, sin relleno. */
@Composable
fun SamaySecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = SamayRadius.md,
        border = BorderStroke(1.dp, SamayForest),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = SamayForest)
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

/** Línea de ayuda. Siempre visible, nunca detrás del paywall. */
@Composable
fun CrisisButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        shape = SamayRadius.md,
        colors = ButtonDefaults.buttonColors(
            containerColor = SamayCrisis,
            contentColor = SamayOnDark
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

/**
 * Opción de audio del kit (poema, música o voz). Seleccionable.
 * @param subtitle autor, duración o "Grabada por …".
 */
@Composable
fun AudioCard(
    title: String,
    subtitle: String?,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    preview: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(SamayRadius.md)
            .background(if (selected) SamaySage else SamaySurface)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) SamayForest else SamayOutline,
                shape = SamayRadius.md
            )
            .clickable(role = Role.RadioButton, onClick = onClick)
            .heightIn(min = SamayMinTouch)
            .padding(SamaySpacing.md)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = SamayForest)
        if (!subtitle.isNullOrBlank()) {
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = SamayMuted)
        }
        if (!preview.isNullOrBlank()) {
            Text(
                preview,
                style = MaterialTheme.typography.bodyMedium,
                color = SamayMuted,
                modifier = Modifier.padding(top = SamaySpacing.sm)
            )
        }
    }
}

/** Persona de confianza: inicial + nombre. Un toque llama o abre el detalle. */
@Composable
fun ContactChip(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(SamayRadius.sm)
            .background(SamaySage)
            .clickable(role = Role.Button, onClick = onClick)
            .heightIn(min = SamayMinTouch)
            .padding(horizontal = SamaySpacing.md, vertical = SamaySpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SamaySpacing.sm)
    ) {
        Box(
            modifier = Modifier.size(28.dp).background(SamayForest, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                name.trim().firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.labelLarge,
                color = SamayOnDark
            )
        }
        Text(name, style = MaterialTheme.typography.titleMedium, color = SamayForest)
    }
}

/** Progreso del onboarding. [current] empieza en 0. */
@Composable
fun ProgressDots(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(SamaySpacing.sm)) {
        repeat(total) { i ->
            Box(
                modifier = Modifier
                    .size(if (i == current) 10.dp else 8.dp)
                    .background(if (i <= current) SamayForest else SamayOutline, CircleShape)
            )
        }
    }
}

/**
 * Círculo de respiración (solo visual). [scale] va de 0.6 (exhalado) a 1.0 (inhalado);
 * la animación y las fases las maneja Modo Terapia (spec en DESIGN_SYSTEM.md).
 */
@Composable
fun BreathCircle(
    scale: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(240.dp).background(SamaySage, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(scale.coerceIn(0.6f, 1f))
                .background(SamayForest, CircleShape)
        )
    }
}

/** Contenedor base de pantalla: fondo crema, respeta barras del sistema, márgenes 24dp. */
@Composable
fun ScreenScaffold(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SamayCream)
            .systemBarsPadding()
            .padding(SamaySpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = SamayForest
        )
        content()
    }
}

// ---------- Previews ----------

@Preview(showBackground = true, backgroundColor = 0xFFF7F3EA)
@Composable
private fun ButtonsPreview() {
    SamayTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SamayButton(text = "Necesito calma", onClick = {})
            SamaySecondaryButton(text = "Mandarle un mensaje", onClick = {})
            CrisisButton(text = "Línea de ayuda gratuita", onClick = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F3EA)
@Composable
private fun KitPreview() {
    SamayTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ProgressDots(total = 6, current = 2)
            AudioCard(title = "Caminante", subtitle = "Antonio Machado", selected = true, onClick = {},
                preview = "Caminante, no hay camino, se hace camino al andar.")
            AudioCard(title = "Lluvia suave", subtitle = "Sonido ambiente", selected = false, onClick = {})
            ContactChip(name = "Mamá", onClick = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F3EA)
@Composable
private fun BreathCirclePreview() {
    SamayTheme { BreathCircle(scale = 0.85f, modifier = Modifier.padding(16.dp)) }
}
