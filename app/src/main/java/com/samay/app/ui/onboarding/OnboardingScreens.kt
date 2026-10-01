package com.samay.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samay.app.data.content.CalmContent
import com.samay.app.data.kit.KitType
import com.samay.app.ui.theme.SamayCream
import com.samay.app.ui.theme.SamayForest
import com.samay.app.ui.theme.SamayMuted
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.samay.app.audio.AudioPlayer
import com.samay.app.audio.RecordingResult
import com.samay.app.audio.VoiceRecorder
import com.samay.app.data.kit.Kit


@Composable
private fun StepScaffold(
    title: String,
    subtitle: String? = null,
    canAdvance: Boolean = true,
    advanceLabel: String = "Continuar",
    onBack: (() -> Unit)? = null,
    onAdvance: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SamayCream)
            .padding(24.dp)
    ) {
        Text(title, color = SamayForest, fontSize = 26.sp, fontWeight = FontWeight.Medium, fontFamily = FontFamily.Serif)
        if (subtitle != null) {
            Spacer(Modifier.height(8.dp))
            Text(subtitle, color = SamayMuted, fontSize = 15.sp, lineHeight = 20.sp)
        }
        Spacer(Modifier.height(20.dp))
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) { content() }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (onBack != null) {
                OutlinedButton(onClick = onBack, modifier = Modifier.height(50.dp)) { Text("Atrás") }
            }
            Button(
                onClick = onAdvance,
                enabled = canAdvance,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SamayForest, contentColor = Color.White)
            ) { Text(advanceLabel, fontWeight = FontWeight.SemiBold) }
        }
    }
}

@Composable
fun WelcomeStep(onStart: () -> Unit) {
    StepScaffold(
        title = "Un lugar para respirar",
        subtitle = "Tu kit personal para los momentos difíciles. Sin buscar, sin pensar: solo accionar.",
        advanceLabel = "Empezar",
        onAdvance = onStart
    ) {}
}

@Composable
fun LanguageStep(selected: String, onSelect: (String) -> Unit, onBack: () -> Unit, onNext: () -> Unit) {
    StepScaffold(
        title = "¿En qué idioma preferís usar la app?",
        onBack = onBack, onAdvance = onNext
    ) {
        listOf("es" to "Español", "en" to "English").forEach { (code, label) ->
            OptionRow(text = label, selected = selected == code) { onSelect(code) }
        }
    }
}

@Composable
fun KitChooseStep(selected: KitType?, onSelect: (KitType) -> Unit, canAdvance: Boolean, onBack: () -> Unit, onNext: () -> Unit) {
    StepScaffold(
        title = "¿Qué te ayuda a calmarte?",
        subtitle = "Elegí el tipo de kit. Después podés cambiarlo en Ajustes.",
        canAdvance = canAdvance, onBack = onBack, onAdvance = onNext
    ) {
        val options = listOf(
            KitType.VOICE to "La voz de una persona de confianza",
            KitType.POEM to "Un versículo o poema",
            KitType.MUSIC to "Música ambiente"
        )
        options.forEach { (type, label) ->
            OptionRow(text = label, selected = selected == type) { onSelect(type) }
        }
    }
}

@Composable
fun KitContentStep(
    items: List<CalmContent>,
    selectedId: String?,
    onSelect: (CalmContent) -> Unit,
    canAdvance: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    StepScaffold(
        title = "Elegí lo que te resuene",
        subtitle = "Contenido de dominio público. Podés cambiarlo cuando quieras.",
        canAdvance = canAdvance, onBack = onBack, onAdvance = onNext
    ) {
        items.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    .selectable(selected = selectedId == item.id) { onSelect(item) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedId == item.id) SamayForest.copy(alpha = 0.10f) else Color.White
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(item.title, color = SamayForest, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    if (item.author.isNotBlank()) {
                        Text(item.author, color = SamayMuted, fontSize = 12.sp)
                    }
                    if (!item.text.isNullOrBlank()) {
                        Spacer(Modifier.height(6.dp))
                        Text(item.text, color = SamayMuted, fontSize = 14.sp, lineHeight = 19.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceStep(
    onVoiceRecorded: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val recorder = remember { VoiceRecorder(context) }
    val player = remember { AudioPlayer(context) }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isRecording by remember { mutableStateOf(false) }
    var recordedPath by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    DisposableEffect(Unit) {
        onDispose {
            if (isRecording) recorder.cancel()
            player.release()
        }
    }

    StepScaffold(
        title = "La voz de tu persona",
        subtitle = if (hasPermission)
            "Grabá un mensaje corto (ej. \"Respirá conmigo\"). Queda solo en tu teléfono, nunca se sube a internet."
        else
            "Necesitamos el micrófono para grabar la voz de tu persona de confianza.",
        onBack = onBack,
        canAdvance = recordedPath != null,
        onAdvance = {
            recordedPath?.let(onVoiceRecorded)
            onNext()
        }
    ) {
        if (!hasPermission) {
            Button(onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }) {
                Text("Dar permiso de micrófono")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onNext) {
                Text("Prefiero usar un poema o música")
            }
            return@StepScaffold
        }

        errorMessage?.let {
            Text(it, color = Color(0xFFB00020), fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
        }

        if (!isRecording && recordedPath == null) {
            Button(onClick = {
                when (recorder.start()) {
                    RecordingResult.STARTED -> { isRecording = true; errorMessage = null }
                    RecordingResult.FAILED_BUSY -> errorMessage = "El micrófono está ocupado por otra app."
                    RecordingResult.FAILED_STORAGE -> errorMessage = "No hay espacio para grabar."
                    RecordingResult.FAILED_UNKNOWN -> errorMessage = "No se pudo empezar a grabar, probá de nuevo."
                }
            }) { Text("● Grabar") }
        }

        if (isRecording) {
            Text("Grabando…", color = SamayForest, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Button(onClick = {
                val path = recorder.stop()
                isRecording = false
                if (path != null) recordedPath = path else errorMessage = "No se pudo guardar la grabación."
            }) { Text("■ Listo") }
        }

        recordedPath?.let { path ->
            Spacer(Modifier.height(16.dp))
            Text("Grabación lista ✓", color = SamayForest)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { player.playKit(Kit(type = KitType.VOICE, voiceFilePath = path)) }) {
                    Text("▶ Escuchar")
                }
                OutlinedButton(onClick = { recordedPath = null; errorMessage = null }) {
                    Text("Grabar de nuevo")
                }
            }
        }
    }
}

@Composable
fun ConfirmStep(
    kitTitle: String,
    contactSummary: String? = null,
    crisisSummary: String? = null,
    saving: Boolean,
    error: String?,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    StepScaffold(
        title = "Tu kit está listo",
        subtitle = "Funciona sin conexión una vez configurado.",
        canAdvance = !saving,
        advanceLabel = if (saving) "Guardando…" else "Ir a la app",
        onBack = onBack, onAdvance = onFinish
    ) {
        SummaryLine("Kit de calma", kitTitle)
        SummaryLine("Persona de confianza", contactSummary ?: "Configurar después (P5)")
        SummaryLine("Línea de crisis", crisisSummary ?: "Según tu país (P5)")
        if (error != null) {
            Spacer(Modifier.height(12.dp))
            Text(error, color = Color(0xFFB00020), fontSize = 13.sp)
        }
    }
}

@Composable
private fun OptionRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp).selectable(selected = selected, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(Modifier.height(0.dp))
        Text(text, color = SamayForest, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
private fun SummaryLine(label: String, value: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, color = SamayMuted, fontSize = 12.sp)
        Text(value, color = SamayForest, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
